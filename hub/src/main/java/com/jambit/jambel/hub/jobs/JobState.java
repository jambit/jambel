package com.jambit.jambel.hub.jobs;

import com.google.common.base.Optional;

public final class JobState {

	public static enum Phase {
		STARTED, COMPLETED, FINISHED;
	}

	public static enum Result {
		SUCCESS, UNSTABLE, FAILURE, NOT_BUILT, ABORTED;
	}

	private final Phase phase;
	private final Optional<Result> result;

	public JobState(Phase phase, Optional<Result> result) {
		this.phase = phase;
		this.result = result;
	}

	public Phase getPhase() {
		return phase;
	}

	public Optional<Result> getResult() {
		return result;
	}

	@Override
	public String toString() {
		String ret = "";
		ret += "job " + phase.toString().toLowerCase();
		if(result.isPresent())
			ret += " with result '" + result.get().toString().toLowerCase() + "'";
		return ret;
	}
}
