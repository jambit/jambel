package com.jambit.jambel.hub.jobs;

import com.google.common.base.Objects;

public final class JobState {

	public static enum Phase {
		STARTED, COMPLETED, FINISHED
	}

	public static enum Result {
		SUCCESS, UNSTABLE, FAILURE, NOT_BUILT, ABORTED
	}

	private final Phase phase;
	private final Result lastResult;

	public JobState(Phase phase, Result lastResult) {
		this.phase = phase;
		this.lastResult = lastResult;
	}

	public Phase getPhase() {
		return phase;
	}

	public Result getLastResult() {
		return lastResult;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("phase", phase).add("last result", lastResult).toString();
	}
}
