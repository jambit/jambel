package com.jambit.jambel.config;

import com.google.common.net.HostAndPort;

public final class SignalLightConfiguration {

	private static class Lights {
		private int green;
		private int yellow;
		private int red;
	}

	private Lights lights;

	private String host;

	private int port;

	private int readTimeout;

	public int getNumberForGreen() {
		return lights.green;
	}

	public int getNumberForYellow() {
		return lights.yellow;
	}

	public int getNumberForRed() {
		return lights.red;
	}

	public HostAndPort getHostAndPort() {
		return HostAndPort.fromParts(host, port);
	}

	public int getReadTimeoutInMs() {
		return readTimeout;
	}

}