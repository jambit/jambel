package com.jambit.jambel.hub;

import com.jambit.jambel.hub.jobs.Job;

import java.net.URL;

public interface JobRetriever {

	Job retrieve(URL jobUrl);

}
