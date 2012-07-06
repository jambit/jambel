package com.jambit.jambel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jambit.jambel.JambelController.JambelStatus;
import com.jambit.jambel.JambelController.LightStatus;

public class JambelTestRun {

	public static void main(String[] args) throws InterruptedException {
		Injector injector = Guice.createInjector(new JambelModule());
		JambelController controller = injector.getInstance(JambelController.class);

		JambelStatus status = new JambelStatus();
		status.green = LightStatus.ON;
		status.yellow = LightStatus.BLINK;
		status.red = LightStatus.OFF;
		controller.setStatus(status);
		JambelStatus currentLightStatus = controller.getCurrentLightStatus();
		System.out.println(currentLightStatus);
		Thread.sleep(2000);
		controller.reset();
	}

}
