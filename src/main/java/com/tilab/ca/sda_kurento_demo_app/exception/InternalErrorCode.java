package com.tilab.ca.sda_kurento_demo_app.exception;

import org.springframework.http.HttpStatus;


public enum InternalErrorCode {
    
    FAILED_TO_FIND_ROOM(4041,HttpStatus.NOT_FOUND),
    FILE_IS_NULL_OR_EMPTY(4011,HttpStatus.BAD_REQUEST),
    FAILED_TO_SAVE_FILE(5001,HttpStatus.INTERNAL_SERVER_ERROR);
    
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
