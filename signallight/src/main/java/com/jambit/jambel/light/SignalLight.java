package com.jambit.jambel.light;

public interface SignalLight {

	enum LightMode {
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

	static class LightStatus {
		public LightMode green = LightMode.OFF;
		public LightMode yellow = LightMode.OFF;
		public LightMode red = LightMode.OFF;

		public LightStatus green(LightMode mode) {
			green = mode;
			return this;
		}

		public LightStatus yellow(LightMode mode) {
			yellow = mode;
			return this;
		}

		public LightStatus red(LightMode mode) {
			red = mode;
			return this;
		}

		@Override
		public String toString() {
			return String.format("green: %s, yellow: %s, red: %s", green, yellow, red);
		}
	}

	public void setNewStatus(LightStatus newStatus);

	// TODO: blink times, ...

	public LightStatus getCurrentStatus();

	public void reset();

}
