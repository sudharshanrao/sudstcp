package com.object;

import java.util.HashMap;

public enum JsonParseFrequency {
	ANNUAL("ANNUAL", "A", 1, "Y"),
	SEMI("SEMI", "S", 6, "M"),
	QUARTERLY("QUARTERLY", "Q", 3, "M"),
	MONTHLY("MONTHLY", "M", 1, "M"),
	ONE_TIME("ONE TIME", "1", 1, "Y");
	
	private String abbreviation;
	private int periodMultiplier;
	private String period;
	private String label;
	
	JsonParseFrequency(String label, String abbreviation, int periodMultiplier, String period) {
		this.label = label; 
		this.abbreviation = abbreviation;
		this.periodMultiplier = periodMultiplier;
		this.period = period;
	}

	public int getPeriodMultiplier() {
		return periodMultiplier;
	}

	public String getPeriod() {
		return period;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getLabel() {
		return label;
	}

	public String getPeriodAndMultiplier() {
		return periodMultiplier + period;
	}

	private static HashMap<String, JsonParseFrequency> lookUp;
	static {
		lookUp = new HashMap<>();
		for (JsonParseFrequency entry : values()) {
			lookUp.put(entry.getPeriodAndMultiplier(), entry);
		}
	}

	public static JsonParseFrequency lookup(String periodAndMultiplier) {
		return lookUp.get(periodAndMultiplier);
	}

	@Override
	/**
	 * This can be used by jackson to parse from JSON so we can more cleanly map
	 * to UI
	 */
	public String toString() {
		return getPeriodAndMultiplier();
	}
}