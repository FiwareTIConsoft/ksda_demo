package com.tilab.ca.sda_kurento_demo_app.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tilab.ca.sda_kurento_demo_app.utils.ZonedDateTimeJsonSerializer;
import java.time.ZonedDateTime;

@JsonInclude(Include.NON_NULL)
public class RoomInfo {
  
    private String roomName;
    
    @JsonSerialize(using = ZonedDateTimeJsonSerializer.class)
    private ZonedDateTime createdAt;
    
    private String thumbUrl;
    
    private int numPartecipants;
    
    public RoomInfo roomName(String roomName){
        this.roomName = roomName;
        return this;
    }
    
    public RoomInfo numPartecipants(int numPartecipants){
        this.numPartecipants = numPartecipants;
        return this;
    }
    
    public RoomInfo createdAt(ZonedDateTime createdAt){
        this.createdAt = createdAt;
        return this;
    }
    
    public RoomInfo thumbUrl(String thumbUrl){
        this.thumbUrl = thumbUrl;
        return this;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getNumPartecipants() {
        return numPartecipants;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
