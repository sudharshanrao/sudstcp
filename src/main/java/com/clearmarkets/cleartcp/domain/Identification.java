package com.clearmarkets.cleartcp.domain;

import java.io.Serializable;

public class Identification implements Serializable{
	private static final long serialVersionUID = 2315336355055817637L;
	private String serviceName;
	private int weight;
	
	public Identification(String serviceName, int weight){
		this.serviceName = serviceName;
		this.weight = weight;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "Identification [serviceName=" + serviceName + ", weight="
				+ weight + "]";
	}
}
