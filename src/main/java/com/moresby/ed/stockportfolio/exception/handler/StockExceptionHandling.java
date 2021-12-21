package com.moresby.ed.stockportfolio.exception.handler;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockExistException;
import com.moresby.ed.stockportfolio.exception.domain.stock.StockNotfoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class StockExceptionHandling extends CommonExceptionHandling{

    @ExceptionHandler(StockExistException.class)
    public ResponseEntity<HttpResponse> stockExistException(Exception exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(StockNotfoundException.class)
    public ResponseEntity<HttpResponse> stockNotFoundException(Exception exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
}
