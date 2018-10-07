package com.cleartcp.enumeration;

public enum RiskMeasure {
	DV01("DV01"),
	PV01("PV01"),
	ACCRUED_INTEREST("ACCRUED_INTEREST"),
	MODIFIED_DURATION("MODIFIED_DURATION"),
	MACAULAY_DURATION("MACAULAY_DURATION"),
	YIELD_TO_MATURITY("YIELD_TO_MATURITY"),
	DIRTY_PRICE("DIRTY_PRICE");
	
	private String label;
	
	private static RiskMeasure[] lookup = values();
    private RiskMeasure(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public static RiskMeasure lookup(int ordinal) {
        return lookup[ordinal];
    }
}
