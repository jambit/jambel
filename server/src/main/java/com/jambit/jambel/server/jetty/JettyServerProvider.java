package com.jambit.jambel.server.jetty;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.jambit.jambel.config.JambelConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.inject.Inject;

public class JettyServerProvider implements Provider<Server> {

	@Inject
	private Injector injector;

	@Inject
	private JambelConfiguration configuration;

	@Override
	public Server get() {
		Server server = new Server(configuration.getHttpPort());
		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.addEventListener(new GuiceServletContextListener() {
			@Override
			protected Injector getInjector() {
				return injector;
			}
		});
		servletContextHandler.setResourceBase("server/webapp/static/");

		servletContextHandler.addFilter(GuiceFilter.class, "/*", null);
		servletContextHandler.addServlet(DefaultServlet.class, "/");
		server.setHandler(servletContextHandler);
		return server;
	}
}
