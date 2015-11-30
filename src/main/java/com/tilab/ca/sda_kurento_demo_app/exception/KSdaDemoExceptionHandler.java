package com.tilab.ca.sda_kurento_demo_app.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class KSdaDemoExceptionHandler extends ResponseEntityExceptionHandler{
    
    private static final Logger log = LoggerFactory.getLogger(KSdaDemoExceptionHandler.class);
    
    protected ResponseEntity<Object> handleNoDataAvailable(RuntimeException e, WebRequest request){
        log.error("Handled exception!",e);
        KSdaDemoException ex=(KSdaDemoException)e;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Error error=new Error(ex.getMessage(), ex.getErrorCode().intValue());
        
        return handleExceptionInternal(e, error, headers, ex.getErrorCode().httpStatus(), request);
    }
    
}
