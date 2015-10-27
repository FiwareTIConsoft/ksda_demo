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
import org.kurento.client.MediaElement;
import org.kurento.room.api.pojo.ParticipantRequest;
import org.kurento.room.exception.RoomException;


public class ExtendedRoomManager extends RoomManager{

    public ExtendedRoomManager(UserNotificationService notificationService, KurentoClientProvider kcProvider) {
        super(notificationService, kcProvider);
    }
    
    @Override
    protected Room newRoom(String roomName, KurentoClient kurentoClient,RoomEventHandler roomEventHandler){
            return new ExtendedRoom(roomName, kurentoClient, roomEventHandler);
    }
    
    @Override
    public void publishMedia(ParticipantRequest request, String sdpOffer,
			boolean doLoopback, MediaElement... mediaElements) {
        Room room = getParticipant(request).getRoom();
        
        if(room.getActivePublishers()>0){
            throw new RoomException(RoomException.Code.EXISTING_USER_IN_ROOM_ERROR_CODE, String.format("In room %s there is already an active publisher",room.getName()));
        }
        super.publishMedia(request, sdpOffer, doLoopback, mediaElements);
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
