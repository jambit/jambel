package com.jambit.jambel.light;

public class SignalLightStatus {

	private final LightMode green;
	private final LightMode yellow;
	private final LightMode red;

	private SignalLightStatus(LightMode green, LightMode yellow, LightMode red) {
		this.green = green;
		this.yellow = yellow;
		this.red = red;
	}

	public static SignalLightStatus individual(LightMode green, LightMode yellow, LightMode red) {
		return new SignalLightStatus(green, yellow, red);
	}

	public static SignalLightStatus all(LightMode mode) {
		return new SignalLightStatus(mode, mode, mode);
	}

	public static SignalLightStatus onlyGreen(LightMode mode) {
		return new SignalLightStatus(mode, LightMode.OFF, LightMode.OFF);
	}

	public SignalLightStatus butGreen(LightMode mode) {
		return new SignalLightStatus(mode, yellow, red);
	}

	public SignalLightStatus butYellow(LightMode mode) {
		return new SignalLightStatus(green, mode, red);
	}

	public SignalLightStatus butRed(LightMode mode) {
		return new SignalLightStatus(green, yellow, mode);
	}

	public static SignalLightStatus onlyYellow(LightMode mode) {
		return new SignalLightStatus(LightMode.OFF, mode, LightMode.OFF);
	}

	public static SignalLightStatus onlyRed(LightMode mode) {
		return new SignalLightStatus(LightMode.OFF, LightMode.OFF, mode);
	}

	public LightMode getGreen() {
		return green;
	}

	public LightMode getYellow() {
		return yellow;
	}

	public LightMode getRed() {
		return red;
	}

	@Override
	public String toString() {
		return String.format("green: %s, yellow: %s, red: %s", green, yellow, red);
	}
}
