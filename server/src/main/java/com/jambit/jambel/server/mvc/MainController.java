package com.jambit.jambel.server.mvc;

import com.jambit.jambel.hub.JobStatusHub;
import org.zdevra.guice.mvc.ModelMap;
import org.zdevra.guice.mvc.annotations.*;

import javax.inject.Inject;

@Controller
public class MainController {

	private final JobStatusHub hub;

	@Inject
	public MainController(JobStatusHub hub) {
		this.hub = hub;
	}

	@Path("/")
	@Model("data")
	@View("main")
	public ModelMap doAction() {
		ModelMap model = new ModelMap();
		model.put("jobs", hub.getLastStates());

		return model;
	}

}
