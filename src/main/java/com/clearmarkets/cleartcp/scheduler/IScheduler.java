package com.clearmarkets.cleartcp.scheduler;

import java.util.Collection;

public interface IScheduler {
	Runnable schedule();
	void add(Runnable runnable, int weight);
	Collection<Runnable> scheduleAll();
	void remove(Runnable runnable);
}
