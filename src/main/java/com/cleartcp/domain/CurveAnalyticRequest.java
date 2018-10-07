package com.cleartcp.domain;

import java.io.Serializable;

public class CurveAnalyticRequest extends AnalyticRequest implements Serializable {
	private static final long serialVersionUID = -1551172802624407970L;
	private double longerLegNotional;
	private double spread;
	public double getLongerLegNotional() {
		return longerLegNotional;
	}
	public void setLongerLegNotional(double longerLegNotional) {
		this.longerLegNotional = longerLegNotional;
	}
	public double getSpread() {
		return spread;
	}
	public void setSpread(double spread) {
		this.spread = spread;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurveAnalyticRequest [longerLegNotional=");
		builder.append(longerLegNotional);
		builder.append(", spread=");
		builder.append(spread);
		builder.append(", getRequestId()=");
		builder.append(getRequestId());
		builder.append(", getSymbol()=");
		builder.append(getSymbol());
		builder.append(", getSecurityType()=");
		builder.append(getSecurityType());
		builder.append(", getCurrency()=");
		builder.append(getCurrency());
		builder.append("]");
		return builder.toString();
	}

}
