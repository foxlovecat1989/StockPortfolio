package com.moresby.ed.stockportfolio.exception.handler;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.exception.domain.watchlist.WachlistNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class WatchlistExceptionHandling extends CommonExceptionHandling{

    @ExceptionHandler(WachlistNotFoundException.class)
    public ResponseEntity<HttpResponse> wachlistNotFoundException(WachlistNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
}
