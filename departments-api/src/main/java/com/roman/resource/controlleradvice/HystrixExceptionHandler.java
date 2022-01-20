package com.roman.resource.controlleradvice;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class HystrixExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<String> hystrix(HystrixRuntimeException ex) {
        String message = ex.getFallbackException().getCause().getCause().getMessage();
        return new ResponseEntity<>( message, HttpStatus.CONFLICT);
    }
}
