package com.jambit.jambel.server;

import javax.inject.Singleton;

import com.google.inject.servlet.ServletModule;
import com.jambit.jambel.hub.jenkins.JenkinsAdapter;
import com.jambit.jambel.server.jetty.HttpServerProvider;
import com.jambit.jambel.server.servlet.JenkinsNotificationsServlet;

public class ServerModule extends ServletModule {

	public static final String JOBS_PATH = "/jobStatusUpdate";

	@Override
	protected void configureServlets() {
		bind(JenkinsAdapter.class).toProvider(new HttpServerProvider()).in(Singleton.class);

		serve(JOBS_PATH).with(JenkinsNotificationsServlet.class);
	}

}
