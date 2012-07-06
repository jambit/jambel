package com.jambit.jambel.cmdsend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.jambit.jambel.JambelConfiguration;
import com.jambit.jambel.JambelController;

/**
 * Sends ASCII commands using a {@link JambelCommandSender}.
 * 
 * @author "Florian Rampp (Florian.Rampp@jambit.com)"
 * 
 */
public class CommandSendingJambelController implements JambelController {

	private final JambelConfiguration configuration;

	private JambelCommandSender commandSender;

	public CommandSendingJambelController(JambelConfiguration configuration, JambelCommandSender commandSender) {
		this.configuration = configuration;
		this.commandSender = commandSender;
	}

	private Integer[] toIntValues(JambelStatus status) {
		Integer[] lightValues = { 0, 0, 0, 0 };
		lightValues[configuration.getNumberForGreen() - 1] = status.green.getCode();
		lightValues[configuration.getNumberForYellow() - 1] = status.yellow.getCode();
		lightValues[configuration.getNumberForRed() - 1] = status.red.getCode();
		return lightValues;
	}

	private JambelStatus toStatus(Integer[] values) {
		JambelStatus status = new JambelStatus();
		status.green = LightStatus.forCode(values[configuration.getNumberForGreen() - 1]);
		status.yellow = LightStatus.forCode(values[configuration.getNumberForYellow() - 1]);
		status.red = LightStatus.forCode(values[configuration.getNumberForRed() - 1]);
		return status;
	}

	private void sendCommand(String command) {
		String response = commandSender.send(command);
		if (!response.equals("OK"))
			throw new RuntimeException("response to command '" + command + "' was response, not 'OK'");
	}

	@Override
	public void setStatus(JambelStatus newStatus) {
		Integer[] lightValues = toIntValues(newStatus);

		sendCommand("set_all=" + Joiner.on(',').join(lightValues));
	}


	@Override
	public JambelStatus getCurrentLightStatus() {
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
	public void reset() {
		sendCommand("reset");
	}

}
