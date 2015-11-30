package com.tilab.ca.sda_kurento_demo_app.internal;

import org.kurento.room.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


public class SdaDataRetrievalTask {
    
    private static final Logger log = LoggerFactory.getLogger(SdaDataRetrievalTask.class);
    
    @Autowired
    private ExtendedRoomManager extendedRoomManager;
    
    @Autowired
    private RoomManager roomManager;
    
    @Scheduled(fixedDelay = 60000) //the scheduler runs every minute
    public void reportCurrentTime() {
       log.info("SCHEDULE ACTIVE!!!!!");
       log.info("Active rooms are:");
       extendedRoomManager.getRoomInfoList().forEach(er -> log.info(er.getRoomName()+"  created at "+er.getCreatedAt()));
    }
    
}
