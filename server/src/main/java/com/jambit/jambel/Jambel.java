package com.jambit.jambel;

import com.google.common.net.HostAndPort;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jambit.jambel.config.ConfigModule;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.hub.HubModule;
import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.server.ServerModule;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jambel {

	private static final Logger logger = LoggerFactory.getLogger(Jambel.class);

	private Injector injector;

	public Jambel(String configFilePath) {
		this.injector = Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule(), new HubModule(), new ServerModule());
	}

	public JambelConfiguration getConfiguration() {
		return injector.getInstance(JambelConfiguration.class);
	}

	public void init() {
		testSignalLightConnection();

		initJobs();

		startServer();
	}

	public void destroy() {
		resetSignalLight();
	}

	private void testSignalLightConnection() {
		SignalLight light = injector.getInstance(SignalLight.class);
		SignalLightConfiguration configuration = injector.getInstance(SignalLightConfiguration.class);

		HostAndPort hostAndPort = configuration.getHostAndPort();
		if (light.isAvailable()) {
			logger.info("signal light is available at {}", hostAndPort);
		} else {
			logger.warn("signal light is not available at {}", hostAndPort);
		}
	}

	private void initJobs() {
		JobStatusHub hub = injector.getInstance(JobStatusHub.class);
		hub.initJobs();
	}


	private void startServer() {
		Server server = injector.getInstance(Server.class);
		JambelConfiguration configuration = injector.getInstance(JambelConfiguration.class);
		try {
			server.start();
			logger.info("started embedded HTTP server listening on port {}", configuration.getHttpPort());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void resetSignalLight() {
		SignalLight light = injector.getInstance(SignalLight.class);
		SignalLightConfiguration configuration = injector.getInstance(SignalLightConfiguration.class);

		HostAndPort hostAndPort = configuration.getHostAndPort();
		if (light.isAvailable()) {
			logger.info("resetting signal light at {}", hostAndPort);
			light.reset();
		}
		else {
			logger.warn("cannot reset signal light at {}, it is not available", hostAndPort);
		}

	}

	public void await() {
		Server server = injector.getInstance(Server.class);
		try {
			server.join();
		} catch (InterruptedException e) {
			// interrupted? => just return
		}
	}

	public static void main(String[] args) {
		final Jambel jambel = new Jambel("etc/jambel.json");

		JambelConfiguration configuration = jambel.getConfiguration();

		logger.info("initializing Jambel");
		jambel.init();
		logger.info("Jambel is ready to receive notifications. " +
				"Be sure to configure Jenkins Notifications plugin " +
				"(https://wiki.jenkins-ci.org/display/JENKINS/Notification+Plugin) for each job " +
				"to HTTP POST to http://<HOSTNAME>:{}{}", configuration.getHttpPort(), ServerModule.JOBS_PATH);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("destroying Jambel");
				jambel.destroy();
			}
		});

		jambel.await();
	}
}