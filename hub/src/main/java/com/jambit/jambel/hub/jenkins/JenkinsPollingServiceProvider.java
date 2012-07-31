package com.jambit.jambel.hub.jenkins;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.google.inject.Provider;

public class JenkinsPollingServiceProvider implements Provider<JenkinsAdapter> {

	@Inject
	private Injector injector;
	
	@Override
	public JenkinsAdapter get() {
		return injector.getInstance(JenkinsPollingService.class);
	}

}
