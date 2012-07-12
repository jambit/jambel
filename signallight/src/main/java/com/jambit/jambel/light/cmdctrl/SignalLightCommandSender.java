package com.jambit.jambel.light.cmdctrl;


public interface SignalLightCommandSender {

	/**
	 * @throws com.jambit.jambel.light.SignalLightNotAvailableException if no connection can be established to the signal light
	 */
	String send(String command);

	boolean reachesSignalLight();

}