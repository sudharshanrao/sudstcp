package com.clearmarkets.object;

public class TestDummyObject {

	private String name;
	private int number;

	public TestDummyObject() {
		super();
	}

	/**
	 * For the test only!
	 * 
	 * @param i
	 * @param string
	 */
	public TestDummyObject(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String hostname) {
		this.name = hostname;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int port) {
		this.number = port;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestDummyObject [hostname=");
		builder.append(name);
		builder.append(", port=");
		builder.append(number);
		builder.append("]");
		return builder.toString();
	}
}