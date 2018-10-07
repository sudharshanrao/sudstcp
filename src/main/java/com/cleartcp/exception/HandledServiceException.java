package com.cleartcp.exception;

public class HandledServiceException extends RuntimeException {

	private static final long serialVersionUID = -4372015174541112660L;

	public HandledServiceException(String message) {
        super(message);
    }

    public HandledServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}    