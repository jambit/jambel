package com.jambit.jambel.server;

import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;

import com.google.inject.servlet.ServletModule;
import com.jambit.jambel.server.jetty.JettyServerProvider;
import com.jambit.jambel.server.servlet.JenkinsNotificationsServlet;

public class ServerModule extends ServletModule {

	public static final String JOBS_PATH = "/jobs";

	@Override
	protected void configureServlets() {
		bind(Server.class).toProvider(new JettyServerProvider()).in(Singleton.class);

		serve(JOBS_PATH).with(JenkinsNotificationsServlet.class);
	}

}
