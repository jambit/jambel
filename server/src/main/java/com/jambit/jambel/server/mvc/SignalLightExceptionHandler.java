package com.jambit.jambel.server.mvc;

import com.google.common.base.Throwables;
import org.zdevra.guice.mvc.ExceptionHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: florian
 */
public class SignalLightExceptionHandler implements ExceptionHandler {

	@Override
	public void handleException(Throwable t, HttpServlet servlet, HttpServletRequest req, HttpServletResponse resp) {
		try {
			PrintWriter writer = new PrintWriter(resp.getOutputStream());
			writer.println(t.getMessage());
			writer.flush();
		}
		catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
