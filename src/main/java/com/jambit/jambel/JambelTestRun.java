package com.jambit.jambel;

import com.jambit.jambel.JambelController.JambelStatus;
import com.jambit.jambel.JambelController.LightStatus;
import com.jambit.jambel.cmdsend.CommandSendingJambelController;
import com.jambit.jambel.cmdsend.lan.LanCommandSender;

public class JambelTestRun {

	public static void main(String[] args) throws InterruptedException {
		CommandSendingJambelController controller = null;
		JambelConfiguration configuration = new JambelConfiguration();
		controller = new CommandSendingJambelController(configuration, new LanCommandSender(
				configuration.getHostAndPort(), configuration.getReadTimeoutInMs()));
		JambelStatus status = new JambelStatus();
		status.green = LightStatus.ON;
		status.yellow = LightStatus.BLINK;
		status.red = LightStatus.OFF;
		controller.setStatus(status);
		JambelStatus currentLightStatus = controller.getCurrentLightStatus();
		System.out.println(currentLightStatus);
		Thread.sleep(2000);
		controller.reset();
	}
}
