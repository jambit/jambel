package com.jambit.jambel.hub.lights;

import com.jambit.jambel.hub.jobs.JobState;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;

public class ResultAggregator {

	public JobState.Result aggregate(Collection<JobState.Result> results) {
		checkArgument(!results.isEmpty(), "there is no result");

		if(results.contains(JobState.Result.FAILURE))
			return JobState.Result.FAILURE;
		if(results.contains(JobState.Result.UNSTABLE))
			return JobState.Result.UNSTABLE;
		if(results.contains(JobState.Result.SUCCESS))
			return JobState.Result.SUCCESS;

		if(results.contains(JobState.Result.ABORTED))
			return JobState.Result.ABORTED;
		if(results.contains(JobState.Result.NOT_BUILT))
			return JobState.Result.NOT_BUILT;

		throw new RuntimeException("unknown results : " + results);
	}

}
