package com.jambit.jambel;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.hub.JobStatusHub;
import com.jambit.jambel.hub.jenkins.JenkinsAdapter;
import com.jambit.jambel.light.SignalLight;
import com.jambit.jambel.light.SignalLightNotAvailableException;

public class JambelInitializer {

	private static final Logger logger = LoggerFactory.getLogger(JambelInitializer.class);

	private final JobStatusHub hub;

	private final SignalLight signalLight;

	private final JenkinsAdapter jenkinsAdapter;

	@Inject
	public JambelInitializer(JobStatusHub hub, SignalLight signalLight, JenkinsAdapter jenkinsAdapter) {
		this.hub = hub;
		this.signalLight = signalLight;
		this.jenkinsAdapter = jenkinsAdapter;
	}

	public void init() {
		testSignalLightConnection();

		initHub();
		
		jenkinsAdapter.startWork();
	}

	private void testSignalLightConnection() {
		SignalLightConfiguration configuration = signalLight.getConfiguration();

		HostAndPort hostAndPort = configuration.getHostAndPort();
		if (signalLight.isAvailable()) {
			logger.info("signal light is available at {}", hostAndPort);
		} else {
			logger.warn("signal light is not available at {}", hostAndPort);
		}
	}

	private void initHub() {
		hub.initJobs();
		try {
			hub.updateSignalLight();
		}
		catch (SignalLightNotAvailableException e) {
			logger.warn("could not update signal light");
		}
	}
}