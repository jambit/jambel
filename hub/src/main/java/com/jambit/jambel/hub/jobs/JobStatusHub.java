package com.jambit.jambel.hub.jobs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
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

	private final Map<Job, JobState.Result> lastResults;


	@Inject
	public JobStatusHub(SignalLight light, LightStatusCalculator calculator) {
		this.light = light;
		this.calculator = calculator;

		this.lastResults = Maps.newLinkedHashMap();
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
		LightStatus newLightStatus = calculator.calc(newState.getPhase(), lastResults.values());
		light.setNewStatus(newLightStatus);
	}

}
