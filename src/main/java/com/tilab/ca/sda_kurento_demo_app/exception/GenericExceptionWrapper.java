package com.tilab.ca.sda_kurento_demo_app.exception;
import java.lang.Exception;

public interface GenericExceptionWrapper<T> {
    
    public T execute() throws Exception ;
}
