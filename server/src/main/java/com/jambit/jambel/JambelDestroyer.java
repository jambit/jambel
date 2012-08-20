package com.jambit.jambel;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.config.SignalLightConfiguration.SlotPosition;
import com.jambit.jambel.light.LightMode;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.SignalLightStatus;

public class JambelDestroyer {

	private static final Logger logger = LoggerFactory.getLogger(JambelDestroyer.class);

	private final SignalLight signalLight;

	@Inject
	public JambelDestroyer(SignalLight signalLight) {
		this.signalLight = signalLight;
	}

	public void destroy() {
		resetSignalLight();
	}

	private void resetSignalLight() {
		SignalLightConfiguration configuration = signalLight.getConfiguration();

		HostAndPort hostAndPort = configuration.getHostAndPort();
		if (signalLight.isAvailable()) {
			logger.info("resetting signal light at {}", hostAndPort);

			SignalLightStatus allOn = SignalLightStatus.all(LightMode.ON);
			sendAndWait(allOn, 2000);
			// top to bottom sequence
			if (signalLight.getConfiguration().getGreen() == SlotPosition.top) {
				sendAndWait(allOn.butGreen(LightMode.OFF), 500);
				sendAndWait(allOn.butGreen(LightMode.OFF).butYellow(LightMode.OFF), 500);
			}
			else {
				sendAndWait(allOn.butRed(LightMode.OFF), 500);
				sendAndWait(allOn.butRed(LightMode.OFF).butYellow(LightMode.OFF), 500);
			}

			signalLight.reset();
		}
		else {
			logger.warn("cannot reset signal light at {}, it is not available", hostAndPort);
		}
	}

	private void sendAndWait(SignalLightStatus lightStatus, int milliseconds) {
		signalLight.setNewStatus(lightStatus);

		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e) {
			// do nothing
		}
	}
}