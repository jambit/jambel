package com.jambit.jambel.config;

import java.net.URL;
import java.util.Collection;

public final class JambelConfiguration {

	private Collection<URL> jobs;

	private SignalLightConfiguration signalLight;

	private int httpPort;

	public Collection<URL> getJobs() {
		return jobs;
	}

	public SignalLightConfiguration getSignalLightConfiguration() {
		return signalLight;
	}

	public int getHttpPort() {
		return httpPort;
	}
}

