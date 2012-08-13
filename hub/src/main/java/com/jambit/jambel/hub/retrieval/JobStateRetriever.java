package com.jambit.jambel.hub.retrieval;

import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;

public interface JobStateRetriever {

	JobState retrieve(Job job);

}
