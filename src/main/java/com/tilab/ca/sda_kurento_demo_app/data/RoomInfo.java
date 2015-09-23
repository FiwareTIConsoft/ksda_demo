package com.tilab.ca.sda_kurento_demo_app.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@JsonInclude(Include.NON_NULL)
public class RoomInfo {
    
    
    private transient static final String DATE_ISO_8601_FORMAT="yyyy-MM-dd'T'HH:mm:ssX";
    private transient static final DateTimeFormatter ISO_8601_WITH_TIMEZONE_FORMATTER = DateTimeFormatter.ofPattern(DATE_ISO_8601_FORMAT);
  
    private String roomName;
    
    //@JsonSerialize(using = ZonedDateTimeJsonSerializer.class)
    private String createdAt;
    
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
        this.createdAt = ISO_8601_WITH_TIMEZONE_FORMATTER.format(createdAt);
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
