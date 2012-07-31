package com.jambit.jambel.hub.jenkins;

public interface JenkinsAdapter {
	
	public void startWork();
	
	public void await();
	
	public void stopWork();
}
