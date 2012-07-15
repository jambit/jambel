package com.jambit.jambel.server.servlet;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.hub.jobs.Job;
import com.jambit.jambel.hub.jobs.JobState;
import org.slf4j.MDC;

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

		public JobState.Phase getPhase() {
			return build.phase;
		}

		public Optional<JobState.Result> getResult() {
			return Optional.fromNullable(build.status);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new Gson();
		NotificationData data = gson.fromJson(new InputStreamReader(req.getInputStream()), NotificationData.class);
		MDC.put("phase", data.build.phase.toString());
		MDC.put("jobName", data.name);
		try {
			hub.updateJobState(data.getJob(), data.getPhase(), data.getResult());
		}
		finally {
			MDC.remove("jobName");
			MDC.remove("phase");
		}
	}

}
