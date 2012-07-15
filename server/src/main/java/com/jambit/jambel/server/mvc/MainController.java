package com.jambit.jambel.server.mvc;

import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.SignalLightNotAvailableException;
import com.jambit.jambel.server.ServerModule;
import org.zdevra.guice.mvc.ModelMap;
import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Model;
import org.zdevra.guice.mvc.annotations.Path;
import org.zdevra.guice.mvc.annotations.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

	private final JobStatusHub hub;
	private final SignalLight light;

	@Inject
	public MainController(JobStatusHub hub, SignalLight light) {
		this.hub = hub;
		this.light = light;
	}

	@Path("/")
	@View("main")
	public ModelMap main(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		model.put("jobs", hub.getLastStates());
		model.put("light", hub.getSignalLight());
		model.put("notificationUrl", request.getRequestURL() + ServerModule.JOBS_PATH);
		model.put("lastStatus", hub.getStatus());

		model.put("resetUrl", request.getRequestURL() + "/light/reset");
		model.put("updateLightUrl", request.getRequestURL() + "/update");

		return model;
	}

	@Path("/update")
	@View("postResult")
	@Model("message")
	public String update() {
		try {
			hub.updateSignalLight();
			return "success";
		}
		catch (SignalLightNotAvailableException e) {
			return e.getMessage();
		}
	}

	@Path("/light/reset")
	@View("postResult")
	@Model("message")
	public String reset() {
		try {
			light.reset();
			return "success";
		}
		catch (SignalLightNotAvailableException e) {
			return e.getMessage();
		}
	}
}
