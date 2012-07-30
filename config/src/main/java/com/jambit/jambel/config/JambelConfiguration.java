package com.jambit.jambel.config;

import java.net.URL;
import java.util.Collection;

public final class JambelConfiguration {

	private Collection<URL> jobs;

	private SignalLightConfiguration signalLight;

	private int httpPort;
	
	private boolean usePolling;
	
	private int pollingInterval;

	public Collection<URL> getJobs() {
		return jobs;
	}

	public SignalLightConfiguration getSignalLightConfiguration() {
		return signalLight;
	}

	public int getHttpPort() {
		return httpPort;
	}
	
	public int getPollingInterval() {
		return pollingInterval;
	}
	
	public boolean isUsePolling() {
		return usePolling;
	}
}

