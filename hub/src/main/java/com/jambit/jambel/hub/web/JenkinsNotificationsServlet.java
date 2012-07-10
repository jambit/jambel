package com.jambit.jambel.hub.web;

import com.google.common.io.CharStreams;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: florian
 * Date: 7/10/12
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class JenkinsNotificationsServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String body = CharStreams.toString(new InputStreamReader(req.getInputStream()));
		System.out.println("HALLO: " + req.getPathTranslated() + ", " + body);
	}
}
