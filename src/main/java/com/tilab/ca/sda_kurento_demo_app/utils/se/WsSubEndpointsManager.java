
package com.tilab.ca.sda_kurento_demo_app.utils.se;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;


public class WsSubEndpointsManager<T> {
    
    private static final Logger log = Logger.getLogger(WsSubEndpointsManager.class);
    
    private final Map<String,WsSubEndpointHandler> subEndpointsHandlerMap = new HashMap<>();
    
    
    public WsSubEndpointsManager<T> registerHandler(String methodName,WsSubEndpointHandler<T> wsSubEndpointHandler){
        subEndpointsHandlerMap.put(methodName, wsSubEndpointHandler);
        return this;
    }
    
    public void handleRequest(String methodName,T params) throws Exception{
        
        if(subEndpointsHandlerMap.containsKey(methodName)){
            subEndpointsHandlerMap.get(methodName).handle(params);
        }
        log.error(String.format("Failed to handle request to method: %s. Unrecognized method.",methodName));
    }
}
