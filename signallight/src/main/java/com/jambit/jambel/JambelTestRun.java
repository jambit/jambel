package com.jambit.jambel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jambit.jambel.light.SignalLight;

public class JambelTestRun {

	public static void main(String[] args) throws InterruptedException {
		Injector injector = Guice.createInjector(new SignalLightModule());
		SignalLight controller = injector.getInstance(SignalLight.class);

		SignalLight.LightStatus status = new SignalLight.LightStatus();
		status.green = SignalLight.LightMode.ON;
		status.yellow = SignalLight.LightMode.BLINK;
		status.red = SignalLight.LightMode.OFF;
		controller.setNewStatus(status);
		SignalLight.LightStatus currentLightStatus = controller.getCurrentStatus();
		System.out.println(currentLightStatus);
		Thread.sleep(2000);
		controller.reset();
	}

}
