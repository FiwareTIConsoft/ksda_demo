package com.tilab.ca.sda_kurento_demo_app.exception;


public class KSdaDemoException extends RuntimeException{

    private InternalErrorCode errorCode;
    
    public KSdaDemoException(InternalErrorCode errorCode,Exception ex) {
        super(ex);
        this.errorCode = errorCode;
    }

    public InternalErrorCode getErrorCode() {
        return errorCode;
    }
}
