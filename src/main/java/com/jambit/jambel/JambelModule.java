package com.jambit.jambel;

import com.google.inject.AbstractModule;
import com.jambit.jambel.cmdsend.CommandSendingJambelController;
import com.jambit.jambel.cmdsend.JambelCommandSender;
import com.jambit.jambel.cmdsend.lan.LanCommandSender;

public class JambelModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JambelConfiguration.class).toInstance(new JambelConfiguration());
		bind(JambelController.class).to(CommandSendingJambelController.class);
		bind(JambelCommandSender.class).to(LanCommandSender.class);
	}

}