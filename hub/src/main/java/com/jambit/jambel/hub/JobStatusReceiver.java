package com.jambit.jambel.hub;

import com.google.common.base.Optional;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;

public interface JobStatusReceiver {

	void updateJobState(Job job, JobState.Phase phase, Optional<JobState.Result> result);

}