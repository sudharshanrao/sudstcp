package com.clearmarkets.cleartcp.domain;

import java.io.Serializable;

public class Bond implements Serializable {
	private static final long serialVersionUID = 7237818447673942762L;
	private String bondYield;
	private String bondPrice;
	private String bondQuantity;
	private String bondSymbol;
	private String bondDV01;
	private String bondReferenceId;
	private String bondCouponRate;
	private String bondMaturityEndDate;
	public String getBondQuantity() {
		return bondQuantity;
	}

	public void setBondQuantity(String bondQuantity) {
		this.bondQuantity = bondQuantity;
	}
	public String getBondYield() {
		return bondYield;
	}

	public void setBondYield(String bondYield) {
		this.bondYield = bondYield;
	}

	public String getBondPrice() {
		return bondPrice;
	}

	public void setBondPrice(String bondPrice) {
		this.bondPrice = bondPrice;
	}
	public String getBondSymbol() {
		return bondSymbol;
	}

	public void setBondSymbol(String bondSymbol) {
		this.bondSymbol = bondSymbol;
	}
	public String getBondDV01() {
		return bondDV01;
	}

	public void setBondDV01(String bondDV01) {
		this.bondDV01 = bondDV01;
	}

	public String getBondReferenceId() {
		return bondReferenceId;
	}

	public void setBondReferenceId(String bondReferenceId) {
		this.bondReferenceId = bondReferenceId;
	}
	
	public String getBondCouponRate() {
		return bondCouponRate;
	}

	public void setBondCouponRate(String bondCouponRate) {
		this.bondCouponRate = bondCouponRate;
	}
	
	public String getBondMaturityEndDate() {
		return bondMaturityEndDate;
	}

	public void setBondMaturityEndDate(String bondMaturityEndDate) {
		this.bondMaturityEndDate = bondMaturityEndDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bond [bondYield=");
		builder.append(bondYield);
		builder.append(", bondPrice=");
		builder.append(bondPrice);
		builder.append(", bondQuantity=");
		builder.append(bondQuantity);
		builder.append(", bondSymbol=");
		builder.append(bondSymbol);
		builder.append(", bondDV01=");
		builder.append(bondDV01);
		builder.append(", bondReferenceId=");
		builder.append(bondReferenceId);
		builder.append(", bondCouponRate=");
		builder.append(bondCouponRate);
		builder.append(", bondMaturityEndDate=");
		builder.append(bondMaturityEndDate);
		builder.append("]");
		return builder.toString();
	}

}
