package com.cleartcp.domain.message;

public enum MessageType {
    HEARTBEAT(0),
	IDENTIFICATION(1),
    APPLICATION(2);
	private int type;
	
	private MessageType(int type) {
        this.type = type;
    }
	public int getType() {
        return type;
    }
	
    private static MessageType[] lookup = values();
    
    public static MessageType lookup(int ordinal) {
        return lookup[ordinal];
    }
}
