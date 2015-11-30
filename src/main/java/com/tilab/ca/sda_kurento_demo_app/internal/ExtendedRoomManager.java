package com.tilab.ca.sda_kurento_demo_app.internal;

import com.tilab.ca.sda_kurento_demo_app.data.RoomInfo;
import static com.tilab.ca.sda_kurento_demo_app.exception.ExceptionHandlingUtils.wrapIfException;
import com.tilab.ca.sda_kurento_demo_app.exception.InternalErrorCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.kurento.room.RoomManager;
//import org.kurento.room.NotificationRoomManager;
import org.kurento.room.exception.RoomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtendedRoomManager{

    @Autowired
    private RoomManager roomManager;
    //private NotificationRoomManager roomManager;
    
    private final Map<String,ExtendedRoom> extRooms = new HashMap<>();
    
    private static final Logger log = LoggerFactory
            .getLogger(ExtendedRoomManager.class);
    
    public void addExtRoomIfNotExists(String roomName){
        if(!extRooms.containsKey(roomName))
            extRooms.put(roomName,new ExtendedRoom(roomName));
    }
    
    public void removeExtRoom(String roomName){
        extRooms.remove(roomName);
    }
    
    
    public void removeClosedRooms(){
        Set<String> rooms = roomManager.getRooms();
        extRooms.forEach((rn,extRoom) -> {
           if(!rooms.contains(rn)){
               log.info("room {} does not exist anymore. Removing from map..",rn);
               extRooms.remove(rn);
           }
        });
    }
    
    
    public List<RoomInfo> getRoomInfoList(){
        return extRooms.values().stream().map(extendedRoom -> {
            return wrapIfException(InternalErrorCode.FAILED_TO_FIND_ROOM,() ->new RoomInfo().roomName(extendedRoom.getName())
                                     .numPartecipants(getNumPartecipants(extendedRoom.getName()))
                                     .createdAt(extendedRoom.getCreatedAt())
                                     .thumbUrl(extendedRoom.getThumbUrl()));
            
        })
        .collect(Collectors.toList());
    }
    
    public RoomInfo getRoomInfo(String roomName) throws Exception{
        if(!extRooms.containsKey(roomName))
            throw new RoomException(RoomException.Code.ROOM_NOT_FOUND_ERROR_CODE, String.format("room %s does not exists",roomName));
        ExtendedRoom extRoom = extRooms.get(roomName);
        return new RoomInfo()
                    .roomName(roomName)
                    .createdAt(extRoom.getCreatedAt())
                    .numPartecipants(getNumPartecipants(extRoom.getName()))
                    .thumbUrl(extRoom.getThumbUrl());
    }
    
    public boolean roomExists(String roomName){
        return extRooms.containsKey(roomName);
    }
    
    public void setRoomThumbUrl(String roomName,String roomThumbUrl){
        if(!extRooms.containsKey(roomName)){
            throw new RoomException(RoomException.Code.ROOM_NOT_FOUND_ERROR_CODE, String.format("room %s does not exists",roomName));
        }
        
        extRooms.get(roomName).setThumbUrl(roomThumbUrl);
    }
    
    public int getNumPartecipants(String roomName) throws Exception{
        return roomManager.getParticipants(roomName).size();
    }
  
}
