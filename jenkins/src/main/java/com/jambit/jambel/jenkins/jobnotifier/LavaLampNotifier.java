package com.jambit.jambel.jenkins.jobnotifier;


import com.jambit.jambel.jenkins.hub.SignalLightHub;
import com.jambit.jambel.jenkins.hub.SignalLightHubDescriptor;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

// FR: this is per project

/**
 * Lava Lamp {@link Notifier}.
 * <p/>
 * <p/>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link LavaLampNotifier} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 * <p/>
 * <p/>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked.
 *
 * @author Ed Randall
 */
public class LavaLampNotifier extends Notifier implements Serializable {
	private static final long serialVersionUID = 3019486479572911590L;

	private static final Logger LOG = Logger.getLogger(LavaLampNotifier.class.getName());

	private final String name;


	// Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
	@DataBoundConstructor
	public LavaLampNotifier(String name) {
		this.name = name;
	}


	public String getName() {
		return this.name;
	}

	@Override
	public boolean needsToRunAfterFinalized() {
		return true;
	}


	/**
	 * This class does explicit check pointing.
	 */
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	public SignalLight[] getSignalLightNames() {
		SignalLightHubDescriptor hub = Hudson.getInstance().getDescriptorByType(SignalLightHubDescriptor.class);
		return hub.getSignalLights();
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
		SignalLightHubDescriptor hub = Hudson.getInstance().getDescriptorByType(SignalLightHubDescriptor.class);

		hub.postNewStatus(name, build);

		return true;
	}

	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

}

