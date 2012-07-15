package com.jambit.jambel;

import com.google.common.net.HostAndPort;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.light.SignalLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

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
			signalLight.reset();
		} else {
			logger.warn("cannot reset signal light at {}, it is not available", hostAndPort);
		}
	}
}