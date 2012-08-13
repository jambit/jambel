package com.jambit.jambel.config;

import java.net.URL;

import com.google.common.base.Optional;

public class JobConfiguration {

	private URL jenkinsJobUrl;

	private UpdateMode updateMode;

	private Integer pollingInterval;

	public URL getJenkinsJobUrl() {
		return jenkinsJobUrl;
	}

	public UpdateMode getUpdateMode() {
		return updateMode;
	}

	public Optional<Integer> getPollingInterval() {
		return Optional.fromNullable(pollingInterval);
	}

}
