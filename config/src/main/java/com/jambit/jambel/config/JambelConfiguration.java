package com.jambit.jambel.config;

import com.google.common.net.HostAndPort;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;

public final class JambelConfiguration {

	private Collection<URL> jobs;

	private SignalLightConfiguration signalLightConfiguration;

	public Collection<URL> getJobs() {
		return jobs;
	}

	public SignalLightConfiguration getSignalLightConfiguration() {
		return signalLightConfiguration;
	}
}

