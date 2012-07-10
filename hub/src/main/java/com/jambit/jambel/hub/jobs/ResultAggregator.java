package com.jambit.jambel.hub.jobs;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.jambit.jambel.hub.jobs.JobState.*;
import static com.jambit.jambel.hub.jobs.JobState.Result.*;

public class ResultAggregator {

	public Result aggregate(Collection<Result> results) {
		checkArgument(!results.isEmpty(), "there is no result");

		if(results.contains(FAILURE))
			return FAILURE;
		if(results.contains(UNSTABLE))
			return UNSTABLE;
		if(results.contains(SUCCESS))
			return SUCCESS;

		if(results.contains(ABORTED))
			return ABORTED;
		if(results.contains(NOT_BUILT))
			return NOT_BUILT;

		throw new RuntimeException("unknown results : " + results);
	}

}
