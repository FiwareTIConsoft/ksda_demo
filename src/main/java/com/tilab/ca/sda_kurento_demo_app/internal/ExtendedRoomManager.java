package com.tilab.ca.sda_kurento_demo_app.internal;

import com.tilab.ca.sda_kurento_demo_app.data.RoomInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.kurento.client.KurentoClient;
import org.kurento.room.RoomManager;
import org.kurento.room.api.KurentoClientProvider;
import org.kurento.room.api.RoomEventHandler;
import org.kurento.room.api.UserNotificationService;
import org.kurento.room.internal.Room;
import static com.tilab.ca.sda_kurento_demo_app.exception.ExceptionHandlingUtils.wrapIfException;
import com.tilab.ca.sda_kurento_demo_app.exception.InternalErrorCode;
import org.kurento.room.exception.RoomException;


public class ExtendedRoomManager extends RoomManager{

    public ExtendedRoomManager(UserNotificationService notificationService, KurentoClientProvider kcProvider) {
        super(notificationService, kcProvider);
    }
    
    @Override
    protected Room newRoom(String roomName, KurentoClient kurentoClient,RoomEventHandler roomEventHandler){
            return new ExtendedRoom(roomName, kurentoClient, roomEventHandler);
    }
    
    //temporaneo da fixare con un wrapper e exception manager
    public List<RoomInfo> getRoomInfoList(){
        return rooms.values().stream().map(room -> {
            ExtendedRoom extendedRoom = (ExtendedRoom) room;
            return wrapIfException(InternalErrorCode.FAILED_TO_FIND_ROOM,() ->new RoomInfo().roomName(extendedRoom.getName())
                                     .numPartecipants(getParticipants(extendedRoom.getName()).size())
                                     .createdAt(extendedRoom.getCreatedAt())
                                     .thumbUrl(extendedRoom.getThumbUrl()));
            
        }).collect(Collectors.toList());
    }
    
    public boolean roomExists(String roomName){
        return rooms.containsKey(roomName);
    }
    
    public void setRoomThumbUrl(String roomName,String roomThumbUrl){
        if(!rooms.containsKey(roomName)){
            throw new RoomException(RoomException.Code.ROOM_NOT_FOUND_ERROR_CODE, String.format("room %s does not exists",roomName));
        }
        
        ((ExtendedRoom)rooms.get(roomName)).setThumbUrl(roomThumbUrl);
    }
    
}
