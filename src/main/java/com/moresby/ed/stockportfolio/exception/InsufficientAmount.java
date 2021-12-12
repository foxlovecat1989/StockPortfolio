package com.moresby.ed.stockportfolio.exception;

public class InsufficientAmount extends RuntimeException{
    public InsufficientAmount(String msg) {
        super(msg);
    }
}
