package com.cleartcp.enumeration;

import java.util.HashMap;

public enum Application {
	ETP("ETP"),
	AFFIRMATION("AFFIRMATION"),
	CLIENT_ADMIN("CLIENT_ADMIN"),
	SUPER_ADMIN("SUPER_ADMIN"),
	QUICK_ADMIN("QUICK_ADMIN"),
	MONITOR_APP("MONITOR_APP"),
	PRICE_PUBLISHER("PRICE_PUBLISHER");

	private String label;
	private static Application[] lookup = values();
    private Application(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public static Application lookup(int ordinal) {
        return lookup[ordinal];
    }

	private static HashMap<String, Application> lookUp;
	static {
		lookUp = new HashMap<>();
		for (Application entry : values()) {
			lookUp.put(entry.getLabel(), entry);
		}
	}

	public static Application lookup(String label) {
		return lookUp.get(label);
	}

}
