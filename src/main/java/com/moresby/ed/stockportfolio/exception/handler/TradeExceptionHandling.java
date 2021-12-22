package com.moresby.ed.stockportfolio.exception.handler;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.exception.domain.trade.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class TradeExceptionHandling extends CommonExceptionHandling{

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<HttpResponse> bankAccountNotFoundException(BankAccountNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InputNumberNegativeException.class)
    public ResponseEntity<HttpResponse> inputNumberNegativeException(InputNumberNegativeException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InSufficientBalanceException.class)
    public ResponseEntity<HttpResponse> inSufficientAmountException(InSufficientBalanceException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InSufficientAmountInInventoryException.class)
    public ResponseEntity<HttpResponse> inSufficientAmountInInventoryException(
            InSufficientAmountInInventoryException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<HttpResponse> inventoryNotFoundException(
            InventoryNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TradeNotFoundException.class)
    public ResponseEntity<HttpResponse> tradeNotFoundException(
            TradeNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
}
