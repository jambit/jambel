package com.jambit.jambel.hub.jobs;

public final class Job {

	private final String name;
	private final String url;

	public Job(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Job)) return false;

		Job job = (Job) o;

		if (name != null ? !name.equals(job.name) : job.name != null) return false;

		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

}
