package com.jambit.jambel.server;

import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.jenkins.JenkinsAdapter;

/**
 * User: florian
 */
@Singleton
public final class HttpServer implements JenkinsAdapter {

	private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

	private final Server server;

	private final JambelConfiguration jambelConfiguration;

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

	@Override
	public void startWork() {
		try {
			server.start();	
			logger.info("Jambel is ready to receive notifications. Be sure to configure Jenkins Notifications plugin (https://wiki.jenkins-ci.org/display/JENKINS/Notification+Plugin) for each job to HTTP POST to http://<HOSTNAME>:{}{}",
					getHttpPort(), ServerModule.JOBS_PATH);

		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stopWork() {
		try {
			server.stop();
			logger.info("stopped embedded HTTP server listening on port {}", getHttpPort());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
