package com.jambit.jambel.hub.retrieval;

import java.io.IOException;
import java.net.URL;

import com.jambit.jambel.hub.jobs.Job;

public interface JobRetriever {

	Job retrieve(URL jobUrl) throws IOException;

}
