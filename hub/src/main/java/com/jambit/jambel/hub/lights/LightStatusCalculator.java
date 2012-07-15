package com.jambit.jambel.hub.lights;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.light.LightMode;

import com.jambit.jambel.light.SignalLightStatus;

public class LightStatusCalculator {

	private final ResultAggregator resultAggregator;

	private final PhaseAggregator phaseAggregator;

	@Inject
	public LightStatusCalculator(ResultAggregator resultAggregator, PhaseAggregator phaseAggregator) {
		this.resultAggregator = resultAggregator;
		this.phaseAggregator = phaseAggregator;
	}

	public SignalLightStatus calc(Iterable<JobState> states) {
		// PHASE
		JobState.Phase aggregatedPhase = aggregatePhase(states);

		LightMode activeLightMode;
		switch (aggregatedPhase) {
			case STARTED:
				activeLightMode = LightMode.BLINK;
				break;
			case COMPLETED:
				activeLightMode = LightMode.ON;
				break;
			case FINISHED:
				activeLightMode = LightMode.ON;
				break;
			default:
				throw new RuntimeException("phase " + aggregatedPhase + " is unknown");
		}

		// RESULT
		JobState.Result aggregatedResult = aggregateLastResult(states);

		switch (aggregatedResult) {
			case SUCCESS:
				return SignalLightStatus.onlyGreen(activeLightMode);
			case UNSTABLE:
				return SignalLightStatus.onlyYellow(activeLightMode);
			case FAILURE:
				return SignalLightStatus.onlyRed(activeLightMode);
			case ABORTED:
				return SignalLightStatus.all(activeLightMode).butYellow(LightMode.OFF);
			case NOT_BUILT:
				return SignalLightStatus.all(activeLightMode).butYellow(LightMode.OFF);
			default:
				throw new RuntimeException("result " + aggregatedResult + " is unknown");
		}
	}

	private JobState.Result aggregateLastResult(Iterable<JobState> states) {
		return resultAggregator.aggregate(FluentIterable.from(states).transform(new Function<JobState, JobState.Result>() {
			@Override
			public JobState.Result apply(JobState input) {
				return input.getLastResult();
			}
		}));
	}

	private JobState.Phase aggregatePhase(Iterable<JobState> states) {
		return phaseAggregator.aggregate(FluentIterable.from(states).transform(new Function<JobState, JobState.Phase>() {
			@Override
			public JobState.Phase apply(JobState input) {
				return input.getPhase();
			}
		}));
	}
}
