package com.jambit.jambel.light;

import com.google.common.net.HostAndPort;

public final class SignalLightNotAvailableException extends RuntimeException {

	private HostAndPort hostAndPort;

	public SignalLightNotAvailableException(HostAndPort hostAndPort, Throwable cause) {
		super("signal light at " + hostAndPort + " is not available", cause);
		this.hostAndPort = hostAndPort;
	}

	public HostAndPort getHostAndPort() {
		return hostAndPort;
	}
}
