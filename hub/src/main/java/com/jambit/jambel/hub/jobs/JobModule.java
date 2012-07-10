package com.jambit.jambel.hub.jobs;

import com.google.inject.AbstractModule;

import java.io.File;

/**
 * User: florian
 */
public class JobModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JobConfiguration.class).toInstance(new JobConfiguration(
				new File("/home/florian/projects/jambit/jambel/hub/src/main/java/com/jambit/jambel/hub/jobs/jobs.json")));
	}

}
