package com.tilab.ca.sda_kurento_demo_app.exception;

import org.springframework.http.HttpStatus;


public enum InternalErrorCode {
    
    FAILED_TO_FIND_ROOM(5001,HttpStatus.INTERNAL_SERVER_ERROR);
    
    private final int internalErrorCode;
    private final HttpStatus httpStatus;
    
    private InternalErrorCode(int internalErrorCode, HttpStatus httpStatus){
        this.internalErrorCode = internalErrorCode;
        this.httpStatus = httpStatus;
    }
    
    public HttpStatus httpStatus(){
        return httpStatus;
    }
    
    public int intValue(){
        return internalErrorCode;
    }
}
