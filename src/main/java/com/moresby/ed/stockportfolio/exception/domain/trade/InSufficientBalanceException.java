package com.moresby.ed.stockportfolio.exception.domain.trade;

public class InSufficientBalanceException extends Exception{
    public InSufficientBalanceException(String message) {
        super(message);
    }
}
