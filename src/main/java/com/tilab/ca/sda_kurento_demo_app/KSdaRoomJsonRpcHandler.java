package com.tilab.ca.sda_kurento_demo_app;

import com.tilab.ca.sda_kurento_demo_app.internal.KSdaNotificationService;
import org.kurento.jsonrpc.Session;
import org.kurento.room.RoomJsonRpcHandler;
import org.springframework.beans.factory.annotation.Autowired;


public class KSdaRoomJsonRpcHandler extends RoomJsonRpcHandler{

    @Autowired
    private KSdaNotificationService notificationService;
    
    @Override
    public void afterConnectionEstablished(Session session) throws Exception {
        super.afterConnectionEstablished(session); 
        notificationService.addNewSession(session);
    }
    
    
}
