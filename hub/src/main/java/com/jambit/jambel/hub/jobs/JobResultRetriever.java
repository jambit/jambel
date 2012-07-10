package com.jambit.jambel.hub.jobs;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: florian
 */
public class JobResultRetriever {

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

	public JobState.Result retrieve(Job job) {
		HttpClient client = new DefaultHttpClient();

		Gson gson = new Gson();
		try {
			HttpResponse jobResponse = client.execute(new HttpGet(job.getUrl() + "/api/json"));
			JsonJob jsonJob = gson.fromJson(new InputStreamReader(
					jobResponse.getEntity().getContent()),
				JsonJob.class);

			HttpResponse lastBuildResponse = client.execute(new HttpGet(jsonJob.lastBuild.url + "/api/json"));
			JsonBuild lastBuild = gson.fromJson(new InputStreamReader(
					lastBuildResponse.getEntity().getContent()),
				JsonBuild.class);

			return lastBuild.result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {
		new JobResultRetriever().retrieve(new Job("test1", "http://localhost:8000/job/test2"));
	}

}
