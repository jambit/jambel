package com.jambit.jambel.server;

import com.google.common.net.HostAndPort;
import com.jambit.jambel.config.JambelConfiguration;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * User: florian
 */
public final class HttpServer {

	private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

	private final Server server;

	private final JambelConfiguration jambelConfiguration;

	@Inject
	public HttpServer(Server server, JambelConfiguration jambelConfiguration) {
		this.server = server;
		this.jambelConfiguration = jambelConfiguration;
	}

	public void await() {
		try {
			server.join();
		}
		catch (InterruptedException e) {
			// interrupted? => just return
		}
	}

	public int getHttpPort() {
		return jambelConfiguration.getHttpPort();
	}

	public void start() {
		try {
			server.start();
			logger.info("started embedded HTTP server listening on port {}", getHttpPort());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
