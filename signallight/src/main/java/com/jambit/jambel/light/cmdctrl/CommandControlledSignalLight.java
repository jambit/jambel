package com.jambit.jambel.light.cmdctrl;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.jambit.jambel.config.SignalLightConfiguration;
import com.jambit.jambel.light.LightMode;
import com.jambit.jambel.light.SignalLightStatus;
import com.jambit.jambel.light.SignalLight;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sends ASCII commands using a {@link SignalLightCommandSender}.
 * 
 * @author "Florian Rampp (Florian.Rampp@jambit.com)"
 * 
 */
public final class CommandControlledSignalLight implements SignalLight {

	private final SignalLightConfiguration configuration;

	private final SignalLightCommandSender commandSender;

	@Inject
	public CommandControlledSignalLight(SignalLightConfiguration configuration, SignalLightCommandSender commandSender) {
		this.configuration = configuration;
		this.commandSender = commandSender;
	}

	private Integer[] toIntValues(SignalLightStatus status) {
		Integer[] lightValues = { 0, 0, 0, 0 };
		lightValues[configuration.getNumberForGreen() - 1] = status.getGreen().getCode();
		lightValues[configuration.getNumberForYellow() - 1] = status.getYellow().getCode();
		lightValues[configuration.getNumberForRed() - 1] = status.getRed().getCode();
		return lightValues;
	}

	private SignalLightStatus toStatus(Integer[] values) {
		LightMode green = LightMode.forCode(values[configuration.getNumberForGreen() - 1]);
		LightMode yellow = LightMode.forCode(values[configuration.getNumberForYellow() - 1]);
		LightMode red = LightMode.forCode(values[configuration.getNumberForRed() - 1]);
		return SignalLightStatus.individual(green, yellow, red);
	}

	private void sendCommand(String command) {
		String response = commandSender.send(command);
		if (!response.equals("OK"))
			throw new RuntimeException("response to command '" + command + "' was '" + response + "', not 'OK'");
	}

	@Override
	public SignalLightStatus getCurrentStatus() {
		String response = commandSender.send("status");
		Pattern statusResponsePattern = Pattern.compile("^status=(\\d),(\\d),(\\d),(\\d),(\\d),(\\d)$");
		Matcher matcher = statusResponsePattern.matcher(response);
		if (matcher.matches()) {
			// the last two digits are ignored
			Integer[] values = new Integer[4];
			for (int i = 0; i < 4; i++) {
				values[i] = Integer.valueOf(matcher.group(i + 1));
			}
			return toStatus(values);
		}
		else {
			throw new RuntimeException("response " + response + " did not match pattern " + statusResponsePattern);
		}
	}

	@Override
	public void setNewStatus(SignalLightStatus newStatus) {
		Integer[] lightValues = toIntValues(newStatus);

		sendCommand("set_all=" + Joiner.on(',').join(lightValues));
	}

	@Override
	public void reset() {
		sendCommand("reset");
	}

	@Override
	public boolean isAvailable() {
		return commandSender.reachesSignalLight();
	}

	@Override
	public SignalLightConfiguration getConfiguration() {
		return configuration;
	}

}
