package com.acciojobs.bms_august.exceptions;

public class BMSUnauthorizedException extends RuntimeException{
    public BMSUnauthorizedException(String message){
        super(message);
    }
}
