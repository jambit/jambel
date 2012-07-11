package com.jambit.jambel.config;

import java.net.URL;
import java.util.Collection;

public final class JambelConfiguration {

	private Collection<URL> jobs;

	private SignalLightConfiguration signalLight;

	public Collection<URL> getJobs() {
		return jobs;
	}

	public SignalLightConfiguration getSignalLightConfiguration() {
		return signalLight;
	}
}

