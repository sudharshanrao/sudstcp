package com.cleartcp.enumeration;

public enum AuthEvent {
	LOGIN("LOGIN"),
	LOGOUT("LOGOUT");
	
	private String label;
	private static AuthEvent[] lookup = values();
    private AuthEvent(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public static AuthEvent lookup(int ordinal) {
        return lookup[ordinal];
    }
}