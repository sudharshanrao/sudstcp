package com.clearmarkets.cleartcp.domain;

import java.io.Serializable;
import java.util.List;

public class SSTAnalyticResponse extends AnalyticResponse implements Serializable {
	private static final long serialVersionUID = -931832807229902156L;
	private String swapSpread;
	private String swapNotional;
	private String swapSymbol;

	private String swapFixedRate;
	private String swapDV01;
	private List<Bond> bondList;

	
	public String getSwapSpread() {
		return swapSpread;
	}

	public void setSwapSpread(String swapSpread) {
		this.swapSpread = swapSpread;
	}


	public String getSwapFixedRate() {
		return swapFixedRate;
	}

	public void setSwapFixedRate(String swapFixedRate) {
		this.swapFixedRate = swapFixedRate;
	}

	public String getSwapSymbol() {
		return swapSymbol;
	}

	public void setSwapSymbol(String swapSymbol) {
		this.swapSymbol = swapSymbol;
	}


	public String getSwapDV01() {
		return swapDV01;
	}

	public void setSwapDV01(String swapDV01) {
		this.swapDV01 = swapDV01;
	}

	public String getSwapNotional() {
		return swapNotional;
	}

	public void setSwapNotional(String swapNotional) {
		this.swapNotional = swapNotional;
	}


	public List<Bond> getBondList() {
		return bondList;
	}


	public void setBondList(List<Bond> bondList) {
		this.bondList = bondList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SSTAnalyticResponse [swapSpread=");
		builder.append(swapSpread);
		builder.append(", swapNotional=");
		builder.append(swapNotional);
		builder.append(", swapSymbol=");
		builder.append(swapSymbol);
		builder.append(", swapFixedRate=");
		builder.append(swapFixedRate);
		builder.append(", swapDV01=");
		builder.append(swapDV01);
		builder.append(", bondList=");
		builder.append(bondList);
		builder.append("]");
		return builder.toString();
	}
}
