package com.tilab.ca.sda_kurento_demo_app.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;


public enum InternalErrorCode {
    
    FAILED_TO_FIND_ROOM(5001,HttpStatus.INTERNAL_SERVER_ERROR);
    
    private final Map<Integer,HttpStatus> internal2StatusMap = new HashMap<>();
    private final int internalErrorCode;
    
    private InternalErrorCode(int internalErrorCode, HttpStatus httpStatus){
        this.internalErrorCode = internalErrorCode;
        internal2StatusMap.put(internalErrorCode, httpStatus);
    }
    
    public HttpStatus httpStatus(){
        return internal2StatusMap.getOrDefault(internalErrorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public int intValue(){
        return internalErrorCode;
    }
}
