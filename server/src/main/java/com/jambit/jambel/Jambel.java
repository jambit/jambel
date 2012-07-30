package com.jambit.jambel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jambit.jambel.config.ConfigModule;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.HubModule;
import com.jambit.jambel.hub.jenkins.JenkinsPollingService;
import com.jambit.jambel.server.HttpServer;
import com.jambit.jambel.server.ServerModule;
import com.jambit.jambel.server.mvc.LimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jambel {

	private static final Logger logger = LoggerFactory.getLogger(Jambel.class);

	private Injector injector;

	public Jambel(String configFilePath) {
		Injector injector = Guice.createInjector(new ConfigModule(configFilePath));
		JambelConfiguration config = injector.getInstance(JambelConfiguration.class);
		if(config.isUsePolling()) {
			this.injector = Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule(),
					new HubModule());
		} else {
			this.injector = Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule(),
					new HubModule(), new ServerModule(), new LimeModule());
		}
	}

	public void init() {
		JambelInitializer initializer = injector.getInstance(JambelInitializer.class);
		initializer.init();
		JambelConfiguration config = injector.getInstance(JambelConfiguration.class);
		if(config.isUsePolling()) {
			initPolling();
		} else {
			initServer();
		}
	}

	public void destroy() {
		JambelDestroyer destroyer = injector.getInstance(JambelDestroyer.class);
		destroyer.destroy();
	}

	public void await() {
		JambelConfiguration config = injector.getInstance(JambelConfiguration.class);
		if(!config.isUsePolling()) {
			HttpServer server = injector.getInstance(HttpServer.class);
			server.await();
		}
	}

	public static void main(String[] args) {
		final Jambel jambel = new Jambel("etc/jambel.json");

		Runtime.getRuntime().addShutdownHook(new Thread("destroyer") {
			@Override
			public void run() {
				logger.info("destroying Jambel");
				jambel.destroy();
			}
		});
		
		logger.info("initializing Jambel");
		jambel.init();

		jambel.await();
	}
	
	private void initServer() {
		HttpServer server = injector.getInstance(HttpServer.class);
		server.start();
		logger.info(
				"Jambel is ready to receive notifications. Be sure to configure Jenkins Notifications plugin (https://wiki.jenkins-ci.org/display/JENKINS/Notification+Plugin) for each job to HTTP POST to http://<HOSTNAME>:{}{}",
				server.getHttpPort(), ServerModule.JOBS_PATH);
	}
	
	private void initPolling() {
		JenkinsPollingService pollingService = injector.getInstance(JenkinsPollingService.class);
		pollingService.startAndWait();
		while(pollingService.isRunning()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}
}