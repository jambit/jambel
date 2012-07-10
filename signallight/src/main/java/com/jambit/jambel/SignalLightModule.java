package com.jambit.jambel;

import com.google.inject.AbstractModule;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.cmdctrl.CommandControlledSignalLight;
import com.jambit.jambel.light.cmdctrl.SignalLightCommandSender;
import com.jambit.jambel.light.cmdctrl.lan.LanCommandSender;

public class SignalLightModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JambelConfiguration.class).toInstance(new JambelConfiguration());
		bind(SignalLight.class).to(CommandControlledSignalLight.class);
		bind(SignalLightCommandSender.class).to(LanCommandSender.class);
	}

}