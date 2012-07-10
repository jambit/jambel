package com.jambit.jambel.jenkins.hub;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Build;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.Arrays;

/**
* Created with IntelliJ IDEA.
* User: florian
* Date: 7/10/12
* Time: 3:13 PM
* To change this template use File | Settings | File Templates.
*/
@Extension
public class SignalLightHubDescriptor extends BuildStepDescriptor<Publisher> {

	private static final String	DEFAULT_ADDRESS = "localhost";
	private static final int	DEFAULT_PORT 	= 1999;

	private SignalLightHub[] installations = new SignalLightHub[0];


    public SignalLightHubDescriptor() {
	    super(SignalLightHub.class);
        load();
    }


    @Override
		public String toString() {
			return "SignalLightHubDescriptor@"+Integer.toHexString(System.identityHashCode(this))
			                    +"["+ Arrays.asList(installations)+"]";
		}


    public String getDefaultAddress() {
	    return DEFAULT_ADDRESS;
    }

    public int getDefaultPort() {
	    return DEFAULT_PORT;
    }


    public SignalLightHub[] getInstallations() {
        return installations.clone();
    }


    public void setInstallations(SignalLightHub... installations) {
        this.installations = installations.clone();
    }


    @Override
    public String getDisplayName() {
        return "Signal Lights";
    }


    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        // indicates that this should not show up in per-project configuration
        return false;
    }


    @Override
    public SignalLightHub newInstance(StaplerRequest req, JSONObject formData) throws FormException {
        return (SignalLightHub)super.newInstance(req, formData);
    }


    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        setInstallations(req.bindJSONToList(clazz, formData.get("tool")).toArray((SignalLightHub[]) Array.newInstance(clazz, 0)));
        save();
        return super.configure(req,formData);
    }SignalLightHubDescriptor


    /**
     * Performs on-the-fly validation of the form field 'address'.
     * @param value
     *      This parameter receives the value that the user has typed.
     * @return
     *      Indicates the outcome of the validation. This is returned to the browser.
     */
    public FormValidation doCheckAddress(@QueryParameter String value) throws IOException, ServletException {
	    try {
		    parseAddress(value);
	    } catch (Exception ex) {
            return FormValidation.error("Unable to resolve server name");
	    }
        return FormValidation.ok();
    }

    public FormValidation doCheckPort(@QueryParameter String value) throws IOException, ServletException {
	    try {
		    parsePort(value);
	    } catch (Exception ex) {
            return FormValidation.error("LavaLamp server port must be an integer 1..65535");
	    }
        return FormValidation.ok();
    }

    public FormValidation doCheckProtocol(@QueryParameter String value) throws IOException, ServletException {
	    try {
		    parseProtocol(value);
	    } catch (Exception ex) {
            return FormValidation.error("LavaLamp server protocol must be [TCP,UDP]");
	    }
        return FormValidation.ok();
    }


    public FormValidation doTestServerConnection(
			    @QueryParameter String address,
			    @QueryParameter String port,
			    @QueryParameter String protocol)
    {
	    Connection c = null;
	    try {
		    InetAddress s = parseAddress(address);
		    int p = parsePort(port);
		    String u = parseProtocol(protocol);

		    if (s.isMulticastAddress()) {
			    if (!SignalLightHub.PROTOCOL_UDP.equalsIgnoreCase(u)) {
			        return FormValidation.error("Protocol must be UDP for Multicast server address");
			    }
		    }

		    c = SignalLightHub.newConnection(s, p, u);
		    c.open();
		    boolean ok = c.ping();
		    String ident = c.getServerIdent();
		    if (ok) {
			    String msg;
			    if (s.isMulticastAddress()) {
			        msg = "Connection to LavaLamp server:"+ident+" tested - check the LavaLampServer log output as no response is sent when using multicast.";
			    } else {
			        msg = "Connection to LavaLamp server:"+ident+" tested successfully.";
			    }
			    return FormValidation.ok(msg);
		    } else {
			    return FormValidation.error("Connected to LavaLamp server:"+ident+" but ping failed.");
		    }
	    } catch (Exception ex) {
            return FormValidation.error("Failed to connect to LavaLamp server at: "+address+":"+port);
	    } finally {
		    if (c != null) {
			    try {
				    c.close();
			    } catch (IOException ix) {
			    }
		    }
	    }
    }

	public void postNewStatus(String name, AbstractBuild<?,?> build) {
	}

	public SignalLight[] getSignalLights() {
		return null;
	}
}
