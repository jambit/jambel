package com.jambit.jambel.hub.jobs;

import com.google.inject.Inject;
import com.jambit.jambel.light.SignalLight;

import java.util.Collection;

import static com.jambit.jambel.hub.jobs.JobState.Result;
import static com.jambit.jambel.light.SignalLight.LightStatus;

public class LightStatusCalculator {

	private final ResultAggregator aggregator;

	@Inject
	public LightStatusCalculator(ResultAggregator aggregator) {
		this.aggregator = aggregator;
	}

	public LightStatus calc(JobState.Phase phase, Collection<Result> results) {
		SignalLight.LightMode activeLightMode;
		switch (phase) {
			case STARTED:
				activeLightMode = SignalLight.LightMode.BLINK;
				break;
			case COMPLETED:
				activeLightMode = SignalLight.LightMode.ON;
				break;
			case FINISHED:
				activeLightMode = SignalLight.LightMode.ON;
				break;
			default:
				throw new RuntimeException("phase " + phase + " is unknown");
		}

		Result aggregatedResult = aggregator.aggregate(results);
		switch (aggregatedResult) {
			case SUCCESS:
				return new LightStatus().green(activeLightMode);
			case UNSTABLE:
				return new LightStatus().yellow(activeLightMode);
			case FAILURE:
				return new LightStatus().red(activeLightMode);
			case ABORTED:
				return new LightStatus().green(activeLightMode).red(activeLightMode);
			case NOT_BUILT:
				return new LightStatus().green(activeLightMode).red(activeLightMode);
			default:
				throw new RuntimeException("result " + aggregatedResult + " is unknown");
		}
	}
}
