package com.jambit.jambel;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
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
		bind(SignalLightCommandSender.class).to(LanCommandSender.class);
	}

	@Provides
	public SignalLightConfiguration config(JambelConfiguration config) {
		return config.getSignalLightConfiguration();
	}

}