package com.jambit.jambel.light.cmdctrl.lan;

import com.google.common.base.Charsets;
import com.google.common.net.HostAndPort;
import com.google.common.util.concurrent.Monitor;
import com.google.inject.Inject;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.light.SignalLightNotAvailableException;
import com.jambit.jambel.light.cmdctrl.SignalLightCommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * The invocations of {@link #send(String)} and {@link #reachesSignalLight()} are synchronized with a fair
 * ordering policy (the thread coming first will enter the monitor first).
 */
public final class LanCommandSender implements SignalLightCommandSender {

	private static final Logger logger = LoggerFactory.getLogger(LanCommandSender.class.getName());

	private final HostAndPort hostAndPort;

	private final int readTimeoutInMs;

	private final Monitor monitor;

	@Inject
	public LanCommandSender(SignalLightConfiguration configuration) {
		this.hostAndPort = configuration.getHostAndPort();
		this.readTimeoutInMs = configuration.getReadTimeoutInMs();

		// a fair monitor is required to keep the order of execution
		this.monitor = new Monitor(true);
	}

	@Override
	public String send(String command) {
		monitor.enter();
		try {
			// open
			Socket connection = connect();
			connection.setSoTimeout(readTimeoutInMs);
			logger.debug("connected to signal light at {}", hostAndPort);

			// command
			Writer writer = new OutputStreamWriter(connection.getOutputStream(), Charsets.US_ASCII);
			writer.write(command + "\r\n");
			writer.flush();

			// response
			Reader reader = new InputStreamReader(connection.getInputStream(), Charsets.US_ASCII);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String response = bufferedReader.readLine();

			// sometimes, the signal light returns "\nOK\r\n => read another line
			if (response.isEmpty())
				response = bufferedReader.readLine();

			logger.debug("sent command '{}' and received response '{}' to signal light at {}",
					new Object[]{command, response, hostAndPort});

			// close
			connection.close();
			logger.debug("disconnected from signal light at {}", hostAndPort);

			return response;
		}
		catch (UnknownHostException e) {
			throw new SignalLightNotAvailableException(hostAndPort, e);
		}
		catch (ConnectException e) {
			throw new SignalLightNotAvailableException(hostAndPort, e);
		}
		catch (IOException e) {
			throw new SignalLightNotAvailableException(hostAndPort, e);
		}
		finally {
			monitor.leave();
		}
	}

	private Socket connect() throws IOException {
		Socket socket = new Socket();
		SocketAddress address = new InetSocketAddress(hostAndPort.getHostText(), hostAndPort.getPort());
		socket.connect(address, readTimeoutInMs);
		return socket;
	}

	@Override
	public boolean reachesSignalLight() {
		monitor.enter();
		try {
			connect().close();
			return true;
		}
		catch (IOException e) {
			return false;
		}
		finally {
			monitor.leave();
		}
	}
}
