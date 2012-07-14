package com.jambit.jambel.hub.lights;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.light.SignalLight;

import static com.jambit.jambel.light.SignalLight.LightStatus;

public class LightStatusCalculator {

	private final ResultAggregator resultAggregator;

	private final PhaseAggregator phaseAggregator;

	@Inject
	public LightStatusCalculator(ResultAggregator resultAggregator, PhaseAggregator phaseAggregator) {
		this.resultAggregator = resultAggregator;
		this.phaseAggregator = phaseAggregator;
	}

	public LightStatus calc(Iterable<JobState> states) {
		// PHASE
		JobState.Phase aggregatedPhase = aggregatePhase(states);

		SignalLight.LightMode activeLightMode;
		switch (aggregatedPhase) {
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
				throw new RuntimeException("phase " + aggregatedPhase + " is unknown");
		}

		// RESULT
		JobState.Result aggregatedResult = aggregateResult(states);

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

	private JobState.Result aggregateResult(Iterable<JobState> states) {
		return resultAggregator.aggregate(FluentIterable.from(states).filter(new Predicate<JobState>() {
			@Override
			public boolean apply(JobState input) {
				return input.getResult().isPresent();
			}
		}).transform(new Function<JobState, JobState.Result>() {
			@Override
			public JobState.Result apply(JobState input) {
				return input.getResult().get();
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
