package com.clearmarkets.cleartcp.enumeration;

public enum SecurityType {
	SST("SST"), //SPREAD_OVER_TREASURY
	CURVE("CURVE"),
	BUTTERFLY("BUTTERFLY");
	
	private String label;
	
	private static SecurityType[] lookup = values();
    private SecurityType(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public static SecurityType lookup(int ordinal) {
        return lookup[ordinal];
    }
}
