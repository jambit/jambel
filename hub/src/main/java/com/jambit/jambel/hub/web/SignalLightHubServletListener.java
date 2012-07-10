package com.jambit.jambel.hub.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.jambit.jambel.SignalLightModule;

public class SignalLightHubServletListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
	    @Override
	    protected void configureServlets() {
		    serve("*").with(JenkinsNotificationsServlet.class);
	    }
    }, new SignalLightModule());
  }
}