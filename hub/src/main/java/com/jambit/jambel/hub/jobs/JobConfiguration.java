package com.jambit.jambel.hub.jobs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;

public class JobConfiguration {

	private final Iterable<Job> jobs;

	public JobConfiguration(File jobFile) {
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<Job>>(){}.getType();
		try {
			jobs = gson.fromJson(new FileReader(jobFile), collectionType);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Iterable<Job> getJobs() {
		return jobs;
	}

}
