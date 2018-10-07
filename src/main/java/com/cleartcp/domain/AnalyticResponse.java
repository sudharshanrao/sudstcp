package com.cleartcp.domain;

public abstract class AnalyticResponse extends AnalyticRequest {
	private Object response;

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
}
