package com.tilab.ca.sda_kurento_demo_app.internal;

import java.time.ZonedDateTime;


public class ExtendedRoom {

    private String name;
    private ZonedDateTime createdAt;
    private String thumbUrl;
    
    public ExtendedRoom(String roomName) {
        this.name = roomName;
        this.createdAt = ZonedDateTime.now();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
