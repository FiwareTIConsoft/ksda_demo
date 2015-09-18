package com.tilab.ca.sda_kurento_demo_app.utils.se;


public interface WsSubEndpointHandler<T> {
    public void handle(T params) throws Exception;
}
