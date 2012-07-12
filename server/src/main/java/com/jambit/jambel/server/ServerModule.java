package com.jambit.jambel.server;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.jambit.jambel.config.JambelConfiguration;
import com.jambit.jambel.server.jetty.JettyServerProvider;
import com.jambit.jambel.server.servlet.JenkinsNotificationsServlet;
import com.jambit.jambel.server.servlet.SignalLightUpdateServlet;
import org.eclipse.jetty.server.Server;

import javax.inject.Singleton;

public class ServerModule extends ServletModule {

	public static final String JOBS_PATH = "/jobs";

	@Override
	protected void configureServlets() {
		bind(Server.class).toProvider(new JettyServerProvider()).in(Singleton.class);

		serve(JOBS_PATH).with(JenkinsNotificationsServlet.class);
		serve("/signalLights/").with(SignalLightUpdateServlet.class);
	}

}
