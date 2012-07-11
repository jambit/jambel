package com.jambit.jambel.config;

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

	@Override
	protected void configure() {
	}

	@Provides
	public JambelConfiguration config() {
		Gson gson = new Gson();
		try {
			return gson.fromJson(new FileReader(new File("/home/florian/projects/jambit/jambel/config/src/main/resources/jambel.json")), JambelConfiguration.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}


