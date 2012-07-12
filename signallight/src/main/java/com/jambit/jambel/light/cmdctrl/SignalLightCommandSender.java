package com.jambit.jambel.light.cmdctrl;


public interface SignalLightCommandSender {

	String send(String command);

	boolean reachesSignalLight();

}