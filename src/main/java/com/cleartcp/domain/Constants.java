package com.clearmarkets.cleartcp.domain;

public class Constants {
	public static final String DEFAULT_TIME_ZONE = "UTC";

	public static final String REQUEST_FOR_LOGIN = "login";
	public static final String REQUEST_FOR_RESET_PASSWORD = "resetPassword";
	public static final int HEARTBEAT_INTERVAL = 20000;
	public static final int MISSED_HEARTBEAT_INTERVAL = HEARTBEAT_INTERVAL * 3; 
	public static final String APPLICATION_ID = "appId";
	public static final int DEFAULT_WEIGHT = 10;
}