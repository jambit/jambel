package com.jambit.jambel.hub.jenkins;

import com.google.gson.Gson;
import com.jambit.jambel.hub.JobResultRetriever;
import com.jambit.jambel.hub.JobRetriever;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JenkinsRetriever implements JobRetriever, JobResultRetriever {

	private static class JsonJob {
		public String name;
		public LastBuild lastBuild;

		private static class LastBuild {
			public int number;
			public String url;
		}
	}

	private static class JsonBuild {
		public int number;
		public JobState.Result result;
	}

	private <T> T getJson(String url, Class<T> clazz) {
		HttpClient client = new DefaultHttpClient();
		Gson gson = new Gson();

		try {
			HttpResponse response = client.execute(new HttpGet(url));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return gson.fromJson(new InputStreamReader(
						response.getEntity().getContent()),
						clazz);
			} else {
				throw new RuntimeException("retrieving JSON object from " + url + " resulted in " + response.getStatusLine().getReasonPhrase());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Job retrieve(URL jobUrl) {
		HttpClient client = new DefaultHttpClient();

		String url = jobUrl + "/api/json";
		JsonJob jsonJob = getJson(url, JsonJob.class);

		return new Job(jsonJob.name, jobUrl.toString());
	}


	@Override
	public JobState.Result retrieve(Job job) {
		String url = job.getUrl() + "/api/json";
		JsonJob jsonJob = getJson(url, JsonJob.class);

		if (jsonJob.lastBuild == null)
			return JobState.Result.NOT_BUILT;

		JsonBuild lastJsonBuild = getJson(jsonJob.lastBuild.url, JsonBuild.class);
		return lastJsonBuild.result;
	}

}
