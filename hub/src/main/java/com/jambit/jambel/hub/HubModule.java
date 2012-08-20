package com.jambit.jambel.hub;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.jambit.jambel.hub.poller.JobStatePoller;
import com.jambit.jambel.hub.retrieval.JobRetriever;
import com.jambit.jambel.hub.retrieval.JobStateRetriever;
import com.jambit.jambel.hub.retrieval.jenkins.JenkinsRetriever;

public class HubModule extends AbstractModule {

	public final static int POLLING_THREADS = 5;

	@Override
	protected void configure() {
		// retrieving
		bind(JobRetriever.class).to(JenkinsRetriever.class);
		bind(JobStateRetriever.class).to(JenkinsRetriever.class);

		// hub
		bind(JobStatusReceiver.class).to(JobStatusHub.class);

		// polling
		bind(JobStatePoller.class);
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("poller-%d").build();
		bind(ScheduledExecutorService.class).annotatedWith(Names.named("poller")).toInstance(
				Executors.newScheduledThreadPool(POLLING_THREADS, namedThreadFactory));
	}

}
