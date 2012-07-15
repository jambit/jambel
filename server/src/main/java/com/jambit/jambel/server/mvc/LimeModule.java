package com.jambit.jambel.server.mvc;

import com.jambit.jambel.light.SignalLightNotAvailableException;
import org.apache.velocity.app.VelocityEngine;
import org.zdevra.guice.mvc.MvcModule;
import org.zdevra.guice.mvc.velocity.VelocityViewPoint;

public class LimeModule extends MvcModule {

	@Override
	protected void configureControllers() {
		VelocityEngine velocity = new VelocityEngine();

//		velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
//		velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
//
		velocity.setProperty("file.resource.loader.path", "server/webapp/templates/");
		velocity.init();
		bind(VelocityEngine.class).toInstance(velocity);

		control("/web/*").withController(MainController.class);
		bindViewName("main").toViewInstance(new VelocityViewPoint("main.velocity"));
		bindViewName("postResult").toViewInstance(new VelocityViewPoint("postResult.velocity"));

		bindException(SignalLightNotAvailableException.class).toHandler(SignalLightExceptionHandler.class);
	}
}
