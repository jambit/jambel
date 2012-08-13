package com.jambit.jambel.hub.retrieval;

import java.io.IOException;

import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;

public interface JobStateRetriever {

	JobState retrieve(Job job) throws IOException;

}
