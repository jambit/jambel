package com.jambit.jambel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.jambit.jambel.config.ConfigModule;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.cmdctrl.CommandControlledSignalLight;
import com.jambit.jambel.light.cmdctrl.SignalLightCommandSender;
import com.jambit.jambel.light.cmdctrl.lan.LanCommandSender;

public class SignalLightModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SignalLight.class).to(CommandControlledSignalLight.class);
		bind(SignalLightCommandSender.class).to(LanCommandSender.class).in(Singleton.class);
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("status-updater-%d").build();
		bind(ScheduledExecutorService.class).annotatedWith(Names.named("signalLight")).toInstance(
				Executors.newSingleThreadScheduledExecutor(namedThreadFactory));
	}

	@Provides
	public SignalLightConfiguration config(JambelConfiguration config) {
		return config.getSignalLightConfiguration();
	}

	/**
	 * Creates a new signal light with the JSON config found at the given path.
	 */
	public static SignalLight create(String configFilePath) {
		return Guice.createInjector(new ConfigModule(configFilePath), new SignalLightModule()).getInstance(SignalLight.class);
	}
	
}