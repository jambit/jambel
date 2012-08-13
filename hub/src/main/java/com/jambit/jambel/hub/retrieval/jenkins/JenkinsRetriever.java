package com.jambit.jambel.hub.retrieval.jenkins;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.retrieval.JobRetriever;
import com.jambit.jambel.hub.retrieval.JobStateRetriever;

public class JenkinsRetriever implements JobRetriever, JobStateRetriever {

	public static final String JENKINS_JSON_API_SUFFIX = "/api/json";

	private static class JsonJob {

		public String name;
		public JsonBuild lastCompletedBuild;
		public JsonBuild lastBuild;

		private static class JsonBuild {

			public String url;
		}
	}

	private static class JsonBuild {

		public boolean building;
		public JobState.Result result;
	}

	private <T> T getJson(String url, Class<T> clazz) throws IOException {
		HttpClient client = new DefaultHttpClient();
		Gson gson = new Gson();

		HttpResponse response = client.execute(new HttpGet(url));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
			String json = CharStreams.toString(reader);
			return gson.fromJson(json, clazz);
		}
		else {
			throw new RuntimeException("retrieving JSON object from " + url + " resulted in "
					+ response.getStatusLine().getReasonPhrase());
		}
	}

	@Override
	public Job retrieve(URL jobUrl) throws IOException {
		String url = jsonUrlFor(jobUrl);
		JsonJob jsonJob = getJson(url, JsonJob.class);

		return new Job(jsonJob.name, jobUrl.toString());
	}


	@Override
	public JobState retrieve(Job job) throws IOException {
		String url = jsonUrlFor(job.getUrl());
		JsonJob jsonJob = getJson(url, JsonJob.class);

		JsonBuild lastBuild = getJson(jsonUrlFor(jsonJob.lastBuild.url), JsonBuild.class);
		JobState.Phase phase = lastBuild.building ? JobState.Phase.STARTED : JobState.Phase.COMPLETED;

		JobState.Result result;
		if (jsonJob.lastCompletedBuild == null)
			result = JobState.Result.NOT_BUILT;
		else {
			JsonBuild lastCompletedBuild = getJson(jsonUrlFor(jsonJob.lastCompletedBuild.url), JsonBuild.class);
			result = lastCompletedBuild.result;
		}
		return new JobState(phase, result);
	}

	private String jsonUrlFor(URL url) {
		return jsonUrlFor(url.toString());
	}

	private String jsonUrlFor(String url) {
		return url + JENKINS_JSON_API_SUFFIX;
	}
}
