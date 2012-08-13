package com.jambit.jambel.hub.init;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.config.JobConfiguration;
import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.retrieval.JobRetriever;
import com.jambit.jambel.hub.retrieval.JobStateRetriever;

@Singleton
public class JobInitializer {

	private static final Logger logger = LoggerFactory.getLogger(JobInitializer.class.getName());

	private final JobStatusHub hub;

	private final JambelConfiguration jambelConfiguration;

	private final JobRetriever jobRetriever;

	private final JobStateRetriever jobStateRetriever;

	@Inject
	public JobInitializer(JobStatusHub hub, JambelConfiguration jambelConfiguration, JobRetriever jobRetriever,
			JobStateRetriever jobStateRetriever) {
		this.hub = hub;
		this.jambelConfiguration = jambelConfiguration;
		this.jobRetriever = jobRetriever;
		this.jobStateRetriever = jobStateRetriever;
	}

	public void initJobs() {
		for (JobConfiguration jobConfig : jambelConfiguration.getJobs()) {
			URL jobUrl = jobConfig.getJenkinsJobUrl();
			try {
				Job job = jobRetriever.retrieve(jobUrl);
				JobState state = jobStateRetriever.retrieve(job);
				hub.addJob(job, state);
				logger.info("initialized job '{}' with state '{}'", job, state);
			}
			catch (RuntimeException e) {
				logger.warn("could not retrieve job or its last build status at {}, permanently removing this job",
						jobUrl);
			}
		}
	}


}
