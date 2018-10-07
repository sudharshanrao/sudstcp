package com.cleartcp.domain;

import java.io.Serializable;

import com.cleartcp.enumeration.CMException;

public class GUIException implements Serializable {
	
	private static final long serialVersionUID = 2406076118647128065L;
	private int exceptionCode;
	private String exceptionDescription;
	private String message;
	private boolean hasMessage;
	
	public GUIException(CMException cmException){
		this(cmException, null);
	}
	
	public GUIException(CMException cmException, String message){
		this.exceptionCode = cmException.getErrorCode();
		this.exceptionDescription = cmException.getLabel();
		this.hasMessage = !(message == null || message.isEmpty());
		if (hasMessage) {
			this.message = message;
		}
	}
	
	public int getExceptionCode() {
		return exceptionCode;
	}
	public String getExceptionDescription() {
		return exceptionDescription;
	}
	
	public boolean hasMessage() {
		return hasMessage;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "GUIException [exceptionCode=" + exceptionCode + ", exceptionDescription=" + exceptionDescription
				+ ", message=" + message + "]";
	}
}