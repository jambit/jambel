package com.jambit.jambel;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.hub.init.JobInitializer;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.SignalLightNotAvailableException;
import com.jambit.jambel.server.HttpServer;
import com.jambit.jambel.server.ServerModule;

public class JambelInitializer {

	private static final Logger logger = LoggerFactory.getLogger(JambelInitializer.class);

	private final JobStatusHub hub;

	private final JobInitializer jobInitializer;

	private final SignalLight signalLight;

	private final HttpServer server;

	@Inject
	public JambelInitializer(JobStatusHub hub, JobInitializer jobInitializer, SignalLight signalLight, HttpServer server) {
		this.hub = hub;
		this.jobInitializer = jobInitializer;
		this.signalLight = signalLight;
		this.server = server;
	}

	public void init() {
		testSignalLightConnection();

		initHub();

		startServer();

		logger.info(
				"Jambel is ready to receive notifications. Be sure to configure Jenkins Notifications plugin (https://wiki.jenkins-ci.org/display/JENKINS/Notification+Plugin) for each job to HTTP POST to http://<HOSTNAME>:{}{}",
				server.getHttpPort(), ServerModule.JOBS_PATH);
	}

	private void testSignalLightConnection() {
		SignalLightConfiguration configuration = signalLight.getConfiguration();

		HostAndPort hostAndPort = configuration.getHostAndPort();
		if (signalLight.isAvailable()) {
			logger.info("signal light is available at {}", hostAndPort);
		}
		else {
			logger.warn("signal light is not available at {}", hostAndPort);
		}
	}

	private void initHub() {
		jobInitializer.initJobs();
		try {
			hub.updateSignalLight();
		}
		catch (SignalLightNotAvailableException e) {
			logger.warn("could not update signal light");
		}
	}

	private void startServer() {
		server.start();
	}
}