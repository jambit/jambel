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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastResult == null) ? 0 : lastResult.hashCode());
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobState other = (JobState) obj;
		if (lastResult != other.lastResult)
			return false;
		if (phase != other.phase)
			return false;
		return true;
	}

}
