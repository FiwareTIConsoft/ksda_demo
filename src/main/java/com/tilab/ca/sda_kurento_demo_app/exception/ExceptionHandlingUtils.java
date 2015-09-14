package com.tilab.ca.sda_kurento_demo_app.exception;

public class ExceptionHandlingUtils {

    public static <T> T wrapIfException(InternalErrorCode errorCode,GenericExceptionWrapper<T> method) {
        try {
            return method.execute();
        } catch (Exception ex) {
            throw new KSdaDemoException(errorCode,ex);
        }
    }
}
