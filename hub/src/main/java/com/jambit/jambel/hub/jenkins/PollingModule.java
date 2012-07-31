package com.jambit.jambel.hub.jenkins;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;

public class PollingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JenkinsAdapter.class).toProvider(new JenkinsPollingServiceProvider()).in(Singleton.class);
	}

}
