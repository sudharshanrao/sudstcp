package com.clearmarkets.cleartcp.domain;

import java.io.Serializable;

import com.clearmarkets.cleartcp.enumeration.Application;
import com.clearmarkets.cleartcp.enumeration.AuthEvent;

public class AuthAudit implements Serializable {
	private static final long serialVersionUID = -1222409911120023395L;
	private String loginId;
	private String sessionId;
	private String userAgent;
	private String clientAddress;
	private AuthEvent authEvent;
	private Application application;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	public AuthEvent getAuthEvent() {
		return authEvent;
	}
	public void setAuthEvent(AuthEvent authEvent) {
		this.authEvent = authEvent;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "AuthAudit [loginId=" + loginId + ", sessionId=" + sessionId
				+ ", userAgent=" + userAgent + ", clientAddress="
				+ clientAddress + ", authEvent=" + authEvent + ", application="
				+ application + "]";
	}
}
