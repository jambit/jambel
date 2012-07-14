package com.jambit.jambel.hub.lights;

import com.google.common.collect.ImmutableSet;
import com.jambit.jambel.hub.jobs.JobState;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public class PhaseAggregator {

	public JobState.Phase aggregate(Iterable<JobState.Phase> phases) {
		Set<JobState.Phase> phaseSet = ImmutableSet.copyOf(phases);
		checkArgument(!phaseSet.isEmpty(), "there is no phase");

		if (phaseSet.contains(JobState.Phase.STARTED))
			return JobState.Phase.STARTED;
		if (phaseSet.contains(JobState.Phase.COMPLETED))
			return JobState.Phase.COMPLETED;
		if (phaseSet.contains(JobState.Phase.FINISHED))
			return JobState.Phase.FINISHED;

		throw new RuntimeException("unknown phases : " + phaseSet);
	}

}
