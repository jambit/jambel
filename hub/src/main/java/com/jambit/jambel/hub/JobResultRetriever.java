package com.jambit.jambel.hub;

import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;

public interface JobResultRetriever {

	JobState.Result retrieve(Job job);

}
