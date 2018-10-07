package com.clearmarkets.cleartcp.domain.message;

import java.io.Serializable;

public abstract class BaseTcpMessage implements Serializable, TcpMessage {
	private static final long serialVersionUID = -8618026128628241322L;

	private String token;
	private String destinationServiceName; // destination serviceName
	private String sourceServiceName; // service from which this message
										// originated from. required for
										// inter-service communication via the
										// routing server.
	private String requestMethod;
	private String sessionId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDestinationServiceName() {
		return destinationServiceName;
	}

	public void setDestinationServiceName(String destinationServiceName) {
		this.destinationServiceName = destinationServiceName;
	}

	public String getSourceServiceName() {
		return sourceServiceName;
	}

	public void setSourceServiceName(String sourceServiceName) {
		this.sourceServiceName = sourceServiceName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseTcpMessage [token=");
		builder.append(token);
		builder.append(", destinationServiceName=");
		builder.append(destinationServiceName);
		builder.append(", sourceServiceName=");
		builder.append(sourceServiceName);
		builder.append(", requestMethod=");
		builder.append(requestMethod);
		builder.append(", sessionId=");
		builder.append(sessionId);
		builder.append("]");
		return builder.toString();
	}

}
