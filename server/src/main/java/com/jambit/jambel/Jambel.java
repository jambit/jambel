package com.jambit.jambel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jambit.jambel.config.ConfigModule;
import com.jambit.jambel.hub.HubModule;
import com.jambit.jambel.server.HttpServer;
import com.jambit.jambel.server.ServerModule;
import com.jambit.jambel.server.mvc.LimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jambel {

	private static final Logger logger = LoggerFactory.getLogger(Jambel.class);

	private Injector injector;

	public Jambel(String configFilePath) {
		this.injector = Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule(),
				new HubModule(), new ServerModule(), new LimeModule());
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
		HttpServer server = injector.getInstance(HttpServer.class);
		server.await();
	}

	public static void main(String[] args) {
		final Jambel jambel = new Jambel("etc/jambel.json");

		logger.info("initializing Jambel");
		jambel.init();

		Runtime.getRuntime().addShutdownHook(new Thread("destroyer") {
			@Override
			public void run() {
				logger.info("destroying Jambel");
				jambel.destroy();
			}
		});

		jambel.await();
	}
}