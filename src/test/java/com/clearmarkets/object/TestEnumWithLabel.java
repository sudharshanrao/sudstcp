package com.clearmarkets.object;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TestEnumWithLabel {


	_1W("1W", 1),
	_1M("1M", 1),
	_2M("2M", 2),
	_3M("3M", 3),
	_4M("4M", 4),
	_5M("5M", 5),
	_6M("6M", 6),
	_1Y("1Y", 1);

	private final String label;
	private final int frequency;
	
	private TestEnumWithLabel(String label, int frequency) {
		this.label = label;
		this.frequency = frequency;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	private static HashMap<String, TestEnumWithLabel> lookUp;
	static {
		lookUp = new HashMap<String, TestEnumWithLabel>();
		for (TestEnumWithLabel tenor : values()) {
			lookUp.put(tenor.getLabel(), tenor);
		}
	}

	public static TestEnumWithLabel lookUp(String label) {
		return lookUp.get(label);
	}

	public int getFrequency() {
		return frequency;
	}

}