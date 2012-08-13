package com.jambit.jambel.hub;

import com.google.inject.AbstractModule;
import com.jambit.jambel.hub.retrieval.JobRetriever;
import com.jambit.jambel.hub.retrieval.JobStateRetriever;
import com.jambit.jambel.hub.retrieval.jenkins.JenkinsRetriever;

public class HubModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JobRetriever.class).to(JenkinsRetriever.class);
		bind(JobStateRetriever.class).to(JenkinsRetriever.class);
	}

}
