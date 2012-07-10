package com.jambit.jambel.jenkins.jobnotifier;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
* Created with IntelliJ IDEA.
* User: florian
* Date: 7/10/12
* Time: 3:36 PM
* To change this template use File | Settings | File Templates.
*/
@Extension
public class DescriptorImpl extends BuildStepDescriptor<Publisher> {

	private SignalLight light;


	public DescriptorImpl() {
		super(LavaLampNotifier.class);
		load();
	}


	@Override
	public String toString() {
		return "SignalLightHubDescriptor@" + Integer.toHexString(System.identityHashCode(this))
				+ "[name=" + lamp.getName() + ",changes=" + lamp.getChangesOnly() + "]";
	}


	public String getDisplayName() {
		return "Lava Lamp Notifications";
	}


	public String getName() {
		if (this.lamp == null)
			return null;
		return this.lamp.getName();
	}

	public boolean getChangesOnly() {
		if (this.lamp == null)
			return false;
		return this.lamp.getChangesOnly();
	}


	/**
	 * indicates that this build step can be used with all types of projects
	 */
	public boolean isApplicable(Class<? extends AbstractProject> jobType) {
		return true;
	}


	public LavaLampInstallation[] getInstallations() {
		LavaLampInstallation[] ins = Hudson.getInstance().getDescriptorByType(LavaLampInstallation.DescriptorImpl.class).getInstallations();
		return ins;
	}


	@Override
	public LavaLampNotifier newInstance(StaplerRequest req, JSONObject formData) throws FormException {
		return (LavaLampNotifier) super.newInstance(req, formData);
	}


	@Override
	public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
		// To persist global configuration information,
		// set that to properties and call save().
		this.lamp = newInstance(req, formData);
		save();
		return super.configure(req, formData);
	}


	public FormValidation doCheckName(@QueryParameter String value) throws IOException, ServletException {
		boolean found = false;
		for (LavaLampInstallation i : getInstallations()) {
			if (value != null && value.equals(i.getName())) {
				found = true;
			}
		}
		if (!found) {
			return FormValidation.error("LavaLamp name:" + value + " not found in global config");
		}
		return FormValidation.ok();
	}


}
