package com.jambit.jambel.hub;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.light.SignalLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

import static com.jambit.jambel.light.SignalLight.LightStatus;

public final class JobStatusHub {

	private static final Logger logger = LoggerFactory.getLogger(JobStatusHub.class);

	private final SignalLight light;
	private final LightStatusCalculator calculator;
	private final JobResultRetriever retriever;

	private final Map<Job, JobState.Result> lastResults;


	@Inject
	public JobStatusHub(SignalLight light, LightStatusCalculator calculator, JobResultRetriever retriever) {
		this.light = light;
		this.calculator = calculator;
		this.retriever = retriever;

		this.lastResults = Maps.newLinkedHashMap();
	}

	public void initJobs(Iterable<Job> jobs) {
		for(Job job : jobs) {
			JobState.Result result = retriever.retrieve(job);
			lastResults.put(job, result);
			logger.info("initialized job '{}' with result '{}'", job, result);
		}
	}

	public void registerJob(Job job, JobState.Result lastResult) {
		lastResults.put(job, lastResult);
	}

	public synchronized void updateJobState(Job job, JobState newState) {
		Preconditions.checkArgument(lastResults.containsKey(job), "job %s has not been registered", job);

		logger.info("job '{}' updated state: {}", job, newState);

		switch (newState.getPhase()) {
			case FINISHED:
				Preconditions.checkArgument(newState.getResult().isPresent(), "job state in phase FINISHED did not contain a result");
				lastResults.put(job, newState.getResult().get());
				break;
			case STARTED:
				break;
			case COMPLETED:
				// TODO: what to do here?
				break;
		}
		updateLightStatus(newState.getPhase());
	}

	public void updateSignalLight() {
		updateLightStatus(JobState.Phase.FINISHED);
	}

	private void updateLightStatus(JobState.Phase currentPhase) {
		LightStatus newLightStatus = calculator.calc(currentPhase, lastResults.values());
		light.setNewStatus(newLightStatus);
	}

}
