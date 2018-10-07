package com.cleartcp.domain;

import java.io.Serializable;

public class CurveAnalyticResponse extends AnalyticResponse implements Serializable {
	private static final long serialVersionUID = 7263980611710435584L;
	private String curveSymbol;
	private String spread;

	private String longerLegNotional;
	private String longerLegDV01;
	private String longerLegFixedRate;

	private String shorterLegNotional;
	private String shorterLegDV01;
	private String shorterLegFixedRate;

	public String getCurveSymbol() {
		return curveSymbol;
	}
	public void setCurveSymbol(String curveSymbol) {
		this.curveSymbol = curveSymbol;
	}
	public String getSpread() {
		return spread;
	}
	public void setSpread(String spread) {
		this.spread = spread;
	}
	public String getLongerLegNotional() {
		return longerLegNotional;
	}
	public void setLongerLegNotional(String longerLegNotional) {
		this.longerLegNotional = longerLegNotional;
	}
	public String getLongerLegDV01() {
		return longerLegDV01;
	}
	public void setLongerLegDV01(String longerLegDV01) {
		this.longerLegDV01 = longerLegDV01;
	}
	public String getLongerLegFixedRate() {
		return longerLegFixedRate;
	}
	public void setLongerLegFixedRate(String longerLegFixedRate) {
		this.longerLegFixedRate = longerLegFixedRate;
	}
	public String getShorterLegNotional() {
		return shorterLegNotional;
	}
	public void setShorterLegNotional(String shorterLegNotional) {
		this.shorterLegNotional = shorterLegNotional;
	}
	public String getShorterLegDV01() {
		return shorterLegDV01;
	}
	public void setShorterLegDV01(String shorterLegDV01) {
		this.shorterLegDV01 = shorterLegDV01;
	}
	public String getShorterLegFixedRate() {
		return shorterLegFixedRate;
	}
	public void setShorterLegFixedRate(String shorterLegFixedRate) {
		this.shorterLegFixedRate = shorterLegFixedRate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurveAnalyticResponse [curveSymbol=");
		builder.append(curveSymbol);
		builder.append(", spread=");
		builder.append(spread);
		builder.append(", longerLegNotional=");
		builder.append(longerLegNotional);
		builder.append(", longerLegDV01=");
		builder.append(longerLegDV01);
		builder.append(", longerLegFixedRate=");
		builder.append(longerLegFixedRate);
		builder.append(", shorterLegNotional=");
		builder.append(shorterLegNotional);
		builder.append(", shorterLegDV01=");
		builder.append(shorterLegDV01);
		builder.append(", shorterLegFixedRate=");
		builder.append(shorterLegFixedRate);
		builder.append("]");
		return builder.toString();
	}
}
