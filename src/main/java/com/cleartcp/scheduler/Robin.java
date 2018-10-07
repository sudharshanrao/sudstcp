package com.clearmarkets.cleartcp.scheduler;

import com.clearmarkets.cleartcp.domain.Constants;

public class Robin {
	private int id;
	private int weight;
	private Runnable runnable;
	
	public Robin(int id, Runnable runnable){
		this(id, runnable, Constants.DEFAULT_WEIGHT);
	}

	public Robin(int id, Runnable runnable, int weight){
		this.id = id;
		this.runnable = runnable;
		this.weight = weight;
	}
	
	public int getWeight(){
		return weight;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Robin [id=" + id + ", weight=" + weight + ", runnable="
				+ runnable + "]";
	}
}
