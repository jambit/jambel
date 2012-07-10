package com.jambit.jambel.hub.web;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.hub.jobs.JobStatusHub;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

@Singleton
public class JenkinsNotificationsServlet extends HttpServlet {

	private final JobStatusHub hub;

	@Inject
	public JenkinsNotificationsServlet(JobStatusHub hub) {
		this.hub = hub;
		this.hub.registerJob(new Job("test1", "job/test1/"), JobState.Result.SUCCESS);
	}

	private static class NotificationData {
		public String name;
		public String url;
		public BuildData build;
		private static class BuildData {
			public String full_url;
			public int number;
			public JobState.Phase phase;
			public JobState.Result status;
			public String url;
		}

		public Job getJob() {
			return new Job(name, url);
		}

		public JobState getJobState() {
			return new JobState(build.phase, Optional.fromNullable(build.status));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new Gson();
		NotificationData data = gson.fromJson(new InputStreamReader(req.getInputStream()), NotificationData.class);
		hub.updateJobState(data.getJob(), data.getJobState());
	}

}
