package com.jambit.jambel.jenkins.hub;

import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.logging.Logger;

// FR: this is the "global module"

/**
 * Represents a single LavaLamp installation on the system.
 */
public class SignalLightHub extends Notifier implements Serializable {

	private static final long serialVersionUID = -1502300463467252323L;

	private static final Logger LOG = Logger.getLogger(SignalLightHub.class.getName());

	private final String name;
	private String connectionType;
	private final String hostname;
	private final int port;

	@DataBoundConstructor
	public SignalLightHub(String name, String connectionType, String hostname, int port) {
		this.name = name;
		this.connectionType = connectionType;
		this.hostname = hostname;
		this.port = port;
	}


	public String getName() {
		return name;
	}


	public int getPort() {
		return port;
	}


	public String getHostname() {
		return hostname;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public String getDescription() {
		StringBuilder sb = new StringBuilder(this.name);
		sb.append(" (");
		sb.append(getHostname());
		sb.append(":");
		sb.append(getPort());
		sb.append(")");
		return sb.toString();
	}

	public Connection newConnection() {
		return newConnection(getInetAddress(), getPort(), getProtocol());
	}

	private static Connection newConnection(InetAddress addr, int port, String protocol) {
		LOG.fine("newConnection(" + protocol + ")");
		if (PROTOCOL_TCP.equalsIgnoreCase(protocol))
			return new TCPConnection(addr, port);
		else
			return new UDPConnection(addr, port);
	}


	@Override
	public boolean needsToRunAfterFinalized() {
		return true;
	}


	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}


	public SignalLightHubDescriptor getDescriptor() {
		SignalLightHubDescriptor descr = (SignalLightHubDescriptor) super.getDescriptor();
		return descr;
	}


}
