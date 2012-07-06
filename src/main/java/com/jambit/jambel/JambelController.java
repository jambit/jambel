package com.jambit.jambel;

public interface JambelController {

	enum LightStatus {
		ON(1), OFF(0), BLINK(2), BLINK_INVERS(4), FLASH(3);

		private final int code;

		private LightStatus(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public static LightStatus forCode(int code) {
			for (LightStatus status : values())
				if (status.getCode() == code)
					return status;
			throw new RuntimeException();
		}
	}

	static class JambelStatus {
		public LightStatus green;
		public LightStatus yellow;
		public LightStatus red;

		@Override
		public String toString() {
			return "green: " + green + ", yellow: " + yellow + ", red: " + red;
		}
	}

	public void setStatus(JambelStatus newStatus);

	// TODO: blink times, ...

	public JambelStatus getCurrentLightStatus();

	public void reset();

}
