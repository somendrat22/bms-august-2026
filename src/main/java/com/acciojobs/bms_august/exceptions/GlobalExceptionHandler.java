package com.acciojobs.bms_august.exceptions;

import com.acciojobs.bms_august.dtos.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BMSUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> generateErrorResponseForUnauthorizedException(BMSUnauthorizedException e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
