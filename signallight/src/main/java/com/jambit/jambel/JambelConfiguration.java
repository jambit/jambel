package com.jambit.jambel;

import com.google.common.net.HostAndPort;

public class JambelConfiguration {

	public int getNumberForGreen() {
		return 1;
	}

	public int getNumberForYellow() {
		return 2;
	}

	public int getNumberForRed() {
		return 3;
	}

	public HostAndPort getHostAndPort() {
		return HostAndPort.fromParts("ampel2.dev", 10001);
	}

	public int getReadTimeoutInMs() {
		return 500;
	}

}
