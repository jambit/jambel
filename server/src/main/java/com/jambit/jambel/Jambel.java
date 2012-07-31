package com.jambit.jambel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jambit.jambel.config.ConfigModule;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.HubModule;
import com.jambit.jambel.hub.jenkins.JenkinsAdapter;
import com.jambit.jambel.hub.jenkins.PollingModule;
import com.jambit.jambel.server.ServerModule;
import com.jambit.jambel.server.mvc.LimeModule;

public class Jambel {

	private static final Logger logger = LoggerFactory.getLogger(Jambel.class);

	private Injector injector;

	public Jambel(String configFilePath) {
		Injector injector = Guice.createInjector(new ConfigModule(configFilePath));
		JambelConfiguration config = injector.getInstance(JambelConfiguration.class);
		if(config.isUsePolling()) {
			this.injector = Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule(),
					new HubModule(), new PollingModule());
		} else {
			this.injector = Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule(),
					new HubModule(), new ServerModule(), new LimeModule());
		}
	}

	public void init() {
		JambelInitializer initializer = injector.getInstance(JambelInitializer.class);
		initializer.init();
	}

	public void destroy() {
		JambelDestroyer destroyer = injector.getInstance(JambelDestroyer.class);
		destroyer.destroy();
	}

	public void await() {
		JenkinsAdapter jenkinsConnectionWorker = injector.getInstance(JenkinsAdapter.class);
		jenkinsConnectionWorker.await();
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
}