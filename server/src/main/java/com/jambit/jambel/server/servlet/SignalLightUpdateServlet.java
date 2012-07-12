package com.jambit.jambel.server.servlet;

import com.jambit.jambel.hub.JobStatusHub;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: florian
 */
@Singleton
public class SignalLightUpdateServlet extends HttpServlet {

	private final JobStatusHub hub;

	@Inject
	public SignalLightUpdateServlet(JobStatusHub hub) {
		this.hub = hub;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		hub.updateSignalLight();
	}
}
