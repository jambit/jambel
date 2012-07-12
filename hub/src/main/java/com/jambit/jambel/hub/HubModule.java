package com.jambit.jambel.hub;

import com.google.inject.AbstractModule;
import com.jambit.jambel.hub.jenkins.JenkinsRetriever;

public class HubModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JobRetriever.class).to(JenkinsRetriever.class);
		bind(JobResultRetriever.class).to(JenkinsRetriever.class);
	}

}
