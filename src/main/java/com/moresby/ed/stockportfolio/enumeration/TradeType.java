package com.moresby.ed.stockportfolio.enumeration;

public enum TradeType {
    SELL("BUY"),
    BUY("SELL");

    private final String type;

    TradeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
