package com.jambit.jambel.hub;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.lights.LightStatusCalculator;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.SignalLightNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.Map;

import static com.jambit.jambel.light.SignalLight.LightStatus;

@Singleton
public final class JobStatusHub {

	private static final Logger logger = LoggerFactory.getLogger(JobStatusHub.class);

	private final SignalLight light;
	private final LightStatusCalculator calculator;
	private final JobRetriever jobRetriever;
	private final JobStateRetriever jobStateRetriever;

	private final Map<Job, JobState> lastResults;
	private final JambelConfiguration jambelConfiguration;


	@Inject
	public JobStatusHub(SignalLight light, LightStatusCalculator calculator, JambelConfiguration jambelConfiguration, JobRetriever jobRetriever, JobStateRetriever jobStateRetriever) {
		this.light = light;
		this.calculator = calculator;
		this.jobRetriever = jobRetriever;
		this.jobStateRetriever = jobStateRetriever;
		this.jambelConfiguration = jambelConfiguration;

		this.lastResults = Maps.newLinkedHashMap();
	}

	public void initJobs() {
		for (URL jobUrl : jambelConfiguration.getJobs()) {
			try {
				Job job = jobRetriever.retrieve(jobUrl);
				JobState state = jobStateRetriever.retrieve(job);
				lastResults.put(job, state);
				logger.info("initialized job '{}' with phase '{}' and result '{}'", new Object[]{job, state.getPhase(), state.getResult()});
			} catch (RuntimeException e) {
				logger.warn("could not retrieve job or its last build status at {}, permanently removing this job", jobUrl);
			}
		}
	}

	public synchronized void updateJobState(Job job, JobState newState) {
		Preconditions.checkArgument(lastResults.containsKey(job), "job %s has not been registered", job);
		logger.debug("job '{}' updated state: {}", job, newState);

		lastResults.put(job, newState);

		updateLightStatus();
	}

	public void updateSignalLight() {
		updateLightStatus();
	}

	private void updateLightStatus() {
		LightStatus newLightStatus = calculator.calc(lastResults.values());
		try {
			light.setNewStatus(newLightStatus);
		} catch (SignalLightNotAvailableException e) {
			logger.warn("could not update signal light with new status '{}'", newLightStatus, e);
		}
	}
}
