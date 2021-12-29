package com.moresby.ed.stockportfolio.exception.domain.stock;

import java.io.IOException;

public class ConnectErrorException extends IOException {
    public ConnectErrorException(String message) {
        super(message);
    }
}
