package com.moresby.ed.stockportfolio.exception.domain.trade;

public class InSufficientAmountInInventoryException extends Exception{
    public InSufficientAmountInInventoryException(String message) {
        super(message);
    }
}
