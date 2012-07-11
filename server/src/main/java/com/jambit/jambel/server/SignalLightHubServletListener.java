package com.jambit.jambel.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.jambit.jambel.SignalLightModule;
import com.jambit.jambel.config.ConfigModule;

public class SignalLightHubServletListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
	    @Override
	    protected void configureServlets() {
		    serve("/jobs/").with(JenkinsNotificationsServlet.class);
		    serve("/signalLights/").with(SignalLightUpdateServlet.class);
	    }
    }, new SignalLightModule(), new ConfigModule());
  }
}