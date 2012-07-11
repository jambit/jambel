package com.jambit.jambel.hub;

import com.google.gson.Gson;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
			String url = job.getUrl() + "/api/json";
			HttpResponse jobResponse = client.execute(new HttpGet(url));
			if (jobResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				JsonJob jsonJob = gson.fromJson(new InputStreamReader(
						jobResponse.getEntity().getContent()),
						JsonJob.class);

				if(jsonJob.lastBuild == null)
					return JobState.Result.NOT_BUILT;

				HttpResponse lastBuildResponse = client.execute(new HttpGet(jsonJob.lastBuild.url + "/api/json"));
				JsonBuild lastBuild = gson.fromJson(new InputStreamReader(
						lastBuildResponse.getEntity().getContent()),
						JsonBuild.class);

				return lastBuild.result;
			}
			else {
				throw new RuntimeException("could not retrieve Jenkins job at " + url);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
