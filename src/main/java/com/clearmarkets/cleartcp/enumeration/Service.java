package com.clearmarkets.cleartcp.enumeration;

import java.util.HashMap;

public enum Service {
	AUTH("auth"),
	GATEWAY_SERVER("gateway-server"),
    ALL("all"),
	MARKET_SERVICE("marketService"),
	AFFIRMATION_SERVICE("affirmationService"),
	ADMIN_SERVICE("adminService"),
	PRICING_SERVICE("pricing-service"),
	PREFERENCE_SERVICE("preferenceService"),
	MONITORING_SERVICE("monitorService"),
	ETP("ETP"),
	TRANSACTION_SERVICE("transaction-service"), 
	PERF_STAT_PUBLISHER("PERF_STAT_PUBLISHER"), 
	STATIC_DATA_SERVICE("staticDataService");	
	
	private String label;
    private static HashMap<String, Service> findByLabel;
	static {
		findByLabel = new HashMap<String, Service>();
		for (Service entry : values()) {
			findByLabel.put(entry.getLabel(), entry);
		}
	}
	
	private Service(String label) {
        this.label = label;
    }

	public String getLabel() {
        return label;
    }
	
    private static Service[] lookup = values();
    
    public static Service lookup(int ordinal) {
        return lookup[ordinal];
    }
    
	public static Service findByLabel(String label) {
		return findByLabel.get(label);
	}
}
