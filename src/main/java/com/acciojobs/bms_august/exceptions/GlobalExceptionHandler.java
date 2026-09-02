package com.acciojobs.bms_august.exceptions;

import com.acciojobs.bms_august.dtos.request.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BMSUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> generateErrorResponseForUnauthorizedException(BMSUnauthorizedException e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
