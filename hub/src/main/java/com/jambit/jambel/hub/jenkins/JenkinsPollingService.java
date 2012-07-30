package com.jambit.jambel.hub.jenkins;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.JobRetriever;
import com.jambit.jambel.hub.JobStateRetriever;
import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;

public class JenkinsPollingService extends AbstractScheduledService {
	
	private final JambelConfiguration jambelConfiguration;
	private final JobRetriever jobRetriever;
	private final JobStateRetriever jobStateRetriever;
	private final JobStatusHub jobStatusHub;
	
	@Inject
	public JenkinsPollingService(JambelConfiguration jambelConfiguration, JobRetriever jobRetriever, JobStateRetriever jobStateRetriever, JobStatusHub jobStatusHub) {
		super();
		this.jambelConfiguration = jambelConfiguration;
		this.jobRetriever = jobRetriever;
		this.jobStateRetriever = jobStateRetriever;
		this.jobStatusHub = jobStatusHub;
	}

	@Override
	protected void runOneIteration() throws Exception {
		for (URL jobUrl : jambelConfiguration.getJobs()) {
			Job job = jobRetriever.retrieve(jobUrl);
			JobState state = jobStateRetriever.retrieve(job);
			jobStatusHub.updateJobState(job, state.getPhase(), Optional.of(state.getLastResult()));
		}
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedDelaySchedule(0, jambelConfiguration.getPollingInterval(), TimeUnit.SECONDS);
	}

}
