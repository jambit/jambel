package com.jambit.jambel.hub.jenkins;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PollingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JenkinsPollingService.class).in(Singleton.class);
	}

}
