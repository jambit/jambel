package com.jambit.jambel.light;

/**
* User: florian
*/
public enum LightMode {
	ON(1), OFF(0), BLINK(2), BLINK_INVERS(4), FLASH(3);

	private final int code;

	private LightMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static LightMode forCode(int code) {
		for (LightMode mode : values())
			if (mode.getCode() == code)
				return mode;

		throw new RuntimeException();
	}
}
