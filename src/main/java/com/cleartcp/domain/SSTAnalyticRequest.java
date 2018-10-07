package com.cleartcp.domain;

import java.io.Serializable;

public class SSTAnalyticRequest extends AnalyticRequest implements Serializable {
	private static final long serialVersionUID = -1410793712161428665L;
	private double swapNotional;
	private double swapSpread;
	public double getSwapNotional() {
		return swapNotional;
	}
	public void setSwapNotional(double swapNotional) {
		this.swapNotional = swapNotional;
	}
	public double getSwapSpread() {
		return swapSpread;
	}
	public void setSwapSpread(double swapSpread) {
		this.swapSpread = swapSpread;
	}
}
