package com.jambit.jambel.cmdsend.lan;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.net.HostAndPort;
import com.jambit.jambel.cmdsend.JambelCommandSender;

public class LanCommandSender implements JambelCommandSender {

	private static final Logger logger = LoggerFactory.getLogger(LanCommandSender.class.getName());

	private final HostAndPort hostAndPort;

	private final int readTimeoutInMs;

	public LanCommandSender(HostAndPort hostAndPort, int readTimeoutInMs) {
		this.hostAndPort = hostAndPort;
		this.readTimeoutInMs = readTimeoutInMs;
	}

	@Override
	public String send(String command) {
		try {
			// open
			Socket connection = new Socket(hostAndPort.getHostText(), hostAndPort.getPort());
			connection.setSoTimeout(readTimeoutInMs);
			logger.debug("connected to jambel at {}", hostAndPort);

			// command
			Writer writer = new OutputStreamWriter(connection.getOutputStream(), Charsets.US_ASCII);
			PrintWriter printer = new PrintWriter(writer);
			printer.println(command);
			// response
			Reader reader = new InputStreamReader(connection.getInputStream(), Charsets.US_ASCII);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String response = bufferedReader.readLine();

			logger.debug("sent command '{}' and received response '{}' to jambel at {}", new Object[] { command,
					response, hostAndPort });

			// close
			connection.close();
			logger.debug("disconnected from jambel at {}", hostAndPort);

			return response;
		}
		catch (UnknownHostException e3) {
			throw Throwables.propagate(e3);
		}
		catch (ConnectException e1) {
			throw new RuntimeException("cannot connect to jambel at " + hostAndPort
					+ ", wrong port or is there another existing connection?", e1);
		}
		catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

}
