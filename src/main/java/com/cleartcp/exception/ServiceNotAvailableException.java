package com.clearmarkets.cleartcp.exception;

public class ServiceNotAvailableException extends Exception {

	private static final long serialVersionUID = -4372015174541112660L;

	public ServiceNotAvailableException(String message) {
        super(message);
    }

    public ServiceNotAvailableException(String message, Throwable throwable) {
        super(message, throwable);
    }
}    