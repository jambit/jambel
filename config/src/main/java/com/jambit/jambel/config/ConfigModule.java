package com.jambit.jambel.config;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * User: florian
 */
public class ConfigModule extends AbstractModule {

	private final String configFilePath;

	public ConfigModule(String configFilePath) {
		this.configFilePath = configFilePath;
		Preconditions.checkArgument(new File(configFilePath).isFile(), "config file not found at " + configFilePath);
	}

	@Override
	protected void configure() {
	}

	@Provides
	public JambelConfiguration config() {
		Gson gson = new Gson();
		try {
			return gson.fromJson(new FileReader(new File(configFilePath)), JambelConfiguration.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}


