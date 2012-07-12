package com.jambit.jambel.hub;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.lights.LightStatusCalculator;
import com.jambit.jambel.light.SignalLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.Map;

import static com.jambit.jambel.light.SignalLight.LightStatus;

public final class JobStatusHub {

	private static final Logger logger = LoggerFactory.getLogger(JobStatusHub.class);

	private final SignalLight light;
	private final LightStatusCalculator calculator;
	private final JobRetriever jobRetriever;
	private final JobResultRetriever jobResultRetriever;

	private final Map<Job, JobState.Result> lastResults;
	private final JambelConfiguration jambelConfiguration;


	@Inject
	public JobStatusHub(SignalLight light, LightStatusCalculator calculator, JambelConfiguration jambelConfiguration, JobRetriever jobRetriever, JobResultRetriever jobResultRetriever) {
		this.light = light;
		this.calculator = calculator;
		this.jobRetriever = jobRetriever;
		this.jobResultRetriever = jobResultRetriever;
		this.jambelConfiguration = jambelConfiguration;

		this.lastResults = Maps.newLinkedHashMap();
	}

	public void initJobs() {
		for(URL jobUrl : jambelConfiguration.getJobs()) {
			try {
				Job job = jobRetriever.retrieve(jobUrl);
				JobState.Result result = jobResultRetriever.retrieve(job);
				lastResults.put(job, result);
				logger.info("initialized job '{}' with result '{}'", job, result);
			} catch (RuntimeException e) {
				logger.warn("could not retrieve job or its last build status at {}, permanently removing this job", jobUrl);
			}
		}
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
