package com.jambit.jambel.hub;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.lights.LightStatusCalculator;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.SignalLightNotAvailableException;
import com.jambit.jambel.light.SignalLightStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

@Singleton
public final class JobStatusHub {

	private static final Logger logger = LoggerFactory.getLogger(JobStatusHub.class);

	private final SignalLight light;
	private final LightStatusCalculator calculator;
	private final JobRetriever jobRetriever;
	private final JobStateRetriever jobStateRetriever;

	private final Map<Job, JobState> lastStates;
	private final JambelConfiguration jambelConfiguration;


	@Inject
	public JobStatusHub(SignalLight light, LightStatusCalculator calculator, JambelConfiguration jambelConfiguration, JobRetriever jobRetriever, JobStateRetriever jobStateRetriever) {
		this.light = light;
		this.calculator = calculator;
		this.jobRetriever = jobRetriever;
		this.jobStateRetriever = jobStateRetriever;
		this.jambelConfiguration = jambelConfiguration;

		this.lastStates = Maps.newLinkedHashMap();
	}

	public Map<Job, JobState> getLastStates() {
		return Collections.unmodifiableMap(lastStates);
	}

	public void initJobs() {
		for (URL jobUrl : jambelConfiguration.getJobs()) {
			try {
				Job job = jobRetriever.retrieve(jobUrl);
				JobState state = jobStateRetriever.retrieve(job);
				lastStates.put(job, state);
				logger.info("initialized job '{}' with state '{}'", job, state);
			} catch (RuntimeException e) {
				logger.warn("could not retrieve job or its last build status at {}, permanently removing this job", jobUrl);
			}
		}
	}

	public void updateSignalLight() {
		updateLightStatus();
	}

	private void updateLightStatus() {
		if(!lastStates.isEmpty()) {
			SignalLightStatus newLightStatus = calculator.calc(lastStates.values());
			try {
				light.setNewStatus(newLightStatus);
				logger.debug("updated signal light with new status '{}'", newLightStatus);
			} catch (SignalLightNotAvailableException e) {
				logger.warn("could not update signal light with new status '{}'", newLightStatus, e);
			}
		}
		else {
			try {
				light.reset();
				logger.debug("reset signal light");
			} catch (SignalLightNotAvailableException e) {
				logger.warn("could not reset signal light", e);
			}
		}
	}

	public void updateJobState(Job job, JobState.Phase phase, Optional<JobState.Result> result) {
		Preconditions.checkArgument(lastStates.containsKey(job), "job %s has not been registered", job);
		JobState newState = null;
		switch (phase) {
			case STARTED:
				logger.info("job '{}' started to build", job);

				// we have no state when phase is starting => use the last result
				newState = new JobState(phase, lastStates.get(job).getLastResult());
				break;
			case FINISHED:
			case COMPLETED:
				logger.info("job '{}' {} to build", job, phase.toString().toLowerCase());

				newState = new JobState(phase, result.get());
				break;
		}


		lastStates.put(job, newState);

		updateLightStatus();
	}

	public SignalLight getSignalLight() {
		return light;
	}

}
