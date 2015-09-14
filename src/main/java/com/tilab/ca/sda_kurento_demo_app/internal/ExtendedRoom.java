package com.tilab.ca.sda_kurento_demo_app.internal;

import java.time.ZonedDateTime;
import org.kurento.client.KurentoClient;
import org.kurento.room.api.RoomHandler;
import org.kurento.room.internal.Room;


public class ExtendedRoom extends Room{

    private ZonedDateTime createdAt;
    private String thumbUrl;
    
    public ExtendedRoom(String roomName, KurentoClient kurentoClient, RoomHandler roomHandler) {
        super(roomName, kurentoClient, roomHandler);
        createdAt = ZonedDateTime.now();
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    
    
    
}
