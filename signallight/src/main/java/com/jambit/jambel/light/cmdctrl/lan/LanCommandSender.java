package com.jambit.jambel.light.cmdctrl.lan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.jambit.jambel.light.cmdctrl.SignalLightCommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import com.jambit.jambel.JambelConfiguration;

public final class LanCommandSender implements SignalLightCommandSender {

	private static final Logger logger = LoggerFactory.getLogger(LanCommandSender.class.getName());

	private final HostAndPort hostAndPort;

	private final int readTimeoutInMs;

	@Inject
	public LanCommandSender(JambelConfiguration configuration) {
		this.hostAndPort = configuration.getHostAndPort();
		this.readTimeoutInMs = configuration.getReadTimeoutInMs();
	}

	@Override
	public String send(String command) {
		try {
			// open
			Socket connection = new Socket(hostAndPort.getHostText(), hostAndPort.getPort());
			connection.setSoTimeout(readTimeoutInMs);
			logger.debug("connected to signal light at {}", hostAndPort);

			// command
			Writer writer = new OutputStreamWriter(connection.getOutputStream(), Charsets.US_ASCII);
			PrintWriter printer = new PrintWriter(writer, true);
			printer.println(command);

			// response
			Reader reader = new InputStreamReader(connection.getInputStream(), Charsets.US_ASCII);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String response = bufferedReader.readLine();

			logger.debug("sent command '{}' and received response '{}' to signal light at {}", new Object[] { command,
					response, hostAndPort });

			// close
			connection.close();
			logger.debug("disconnected from signal light at {}", hostAndPort);

			return response;
		}
		catch (UnknownHostException e) {
			throw Throwables.propagate(e);
		}
		catch (ConnectException e) {
			throw new RuntimeException("cannot connect to signal light at " + hostAndPort
					+ ", wrong port or is there another open connection to this signal light?", e);
		}
		catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

}
