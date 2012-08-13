package com.jambit.jambel.hub.poller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.jambit.jambel.hub.JobStatusReceiver;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.retrieval.JobStateRetriever;

/**
 * Uses the given {@link JobStateRetriever} to update the {@link Job} state for the given
 * {@link JobStatusReceiver}.
 * 
 * @author "Florian Rampp (Florian.Rampp@jambit.com)"
 * 
 */
public final class PollTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(PollTask.class.getName());

	private final Job job;

	private final JobStateRetriever retriever;

	private final JobStatusReceiver receiver;


	public PollTask(Job job, JobStateRetriever retriever, JobStatusReceiver receiver) {
		this.job = job;
		this.retriever = retriever;
		this.receiver = receiver;
	}

	@Override
	public void run() {
		JobState jobState;
		try {
			jobState = retriever.retrieve(job);
			logger.debug("retrieved job state {} for job {}", jobState, job);
			receiver.updateJobState(job, jobState.getPhase(), Optional.fromNullable(jobState.getLastResult()));
		}
		catch (IOException e) {
			logger.warn("could not retrieve job state for job {}", job, e);
			// do nothing else. If polling fails, Jenkins might just be temporarily down
		}
	}

}
