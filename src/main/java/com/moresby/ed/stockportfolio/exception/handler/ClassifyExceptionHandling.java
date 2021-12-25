package com.moresby.ed.stockportfolio.exception.handler;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNameExistException;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ClassifyExceptionHandling extends CommonExceptionHandling{

    @ExceptionHandler(ClassifyNameExistException.class)
    public ResponseEntity<HttpResponse> classifyNameExistException(ClassifyNameExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ClassifyNotFoundException.class)
    public ResponseEntity<HttpResponse> classifyNotFoundException(ClassifyNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
}
