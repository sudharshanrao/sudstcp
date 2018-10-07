package com.cleartcp.enumeration;

import java.io.Serializable;

public enum CMException implements Serializable {
	SERVICE_NOT_AVAILABLE(100, "Service not available"),
	ROUTING_SERVER_NOT_AVAILABLE(200, "Routing Server not available"),
	EXCEPTION_WHILE_PROCESSING_REQUEST(300, "Exception while processing request"),
	USER_ALREADY_LOGGED_IN(400, "User already logged in"),
	INVALID_USER_PASSWORD(500, "Invalid username/password"),
	INVALID_APPID(600, "Invalid applicationId"),
	ROUTER_NOT_CONNECTED_TO_GATEWAY(700, "Router not connected to Gateway."),
	INVALID_TOKEN_ON_REQUEST(800,"Invalid token on request");
	
	private String label;
	private int errorCode;
	
	private static CMException[] lookup = values();
    private CMException(int errorCode, String label) {
        this.label = label;
        this.errorCode = errorCode;
    }
    public String getLabel() {
        return label;
    }
    public static CMException lookup(int ordinal) {
        return lookup[ordinal];
    }
	public int getErrorCode() {
		return errorCode;
	}
}