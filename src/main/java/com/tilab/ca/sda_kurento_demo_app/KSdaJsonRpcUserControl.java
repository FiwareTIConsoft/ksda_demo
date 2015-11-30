package com.tilab.ca.sda_kurento_demo_app;

import com.google.gson.JsonObject;
import com.tilab.ca.sda_kurento_demo_app.internal.ExtendedRoomManager;
import com.tilab.ca.sda_kurento_demo_app.internal.KSdaNotificationService;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.kurento.jsonrpc.Transaction;
import org.kurento.jsonrpc.message.Request;
import org.kurento.room.api.pojo.ParticipantRequest;
import org.kurento.room.exception.AdminException;
import org.kurento.room.exception.RoomException;
import org.kurento.room.rpc.JsonRpcProtocolElements;
import org.kurento.room.rpc.JsonRpcUserControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class KSdaJsonRpcUserControl extends JsonRpcUserControl {

    private static final Logger log = LoggerFactory
            .getLogger(KSdaJsonRpcUserControl.class);
    
    
    @Autowired
    private ExtendedRoomManager extendedRoomManager;
    
    @Autowired
    private KSdaNotificationService notificationService;

    private static final String SUB_METHOD_JPARAM = "subMethod";
    
    private static final String USERS_LIST_JPARAM = "roomsList";

    
    @Override
    public void leaveRoom(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest) throws AdminException{
        notificationService.removeSession(transaction.getSession());
        super.leaveRoom(transaction, request, participantRequest); 
        
        if(request!=null && request.getParams()!=null){
            log.info("request is {}",request.toString());
            String roomName = getStringParam(request,JsonRpcProtocolElements.JOIN_ROOM_ROOM_PARAM);
            extendedRoomManager.removeExtRoom(roomName);
        }else{
            log.warn("Cannot check if an extRoom has to be closed since no request info are provided");
            extendedRoomManager.removeClosedRooms();
        }
        notificationService.notifyAllActiveSessions(USERS_LIST_JPARAM, extendedRoomManager.getRoomInfoList());
    }

    @Override
    public void leaveRoomAfterConnClosed(String sessionId) {
        super.leaveRoomAfterConnClosed(sessionId); //To change body of generated methods, choose Tools | Templates.
        extendedRoomManager.removeClosedRooms();
        notificationService.removeSession(sessionId);
        notificationService.notifyAllActiveSessions(USERS_LIST_JPARAM, extendedRoomManager.getRoomInfoList());
    }
      

    @Override
    public void joinRoom(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest) throws IOException, InterruptedException, ExecutionException {
        String roomName = getStringParam(request,JsonRpcProtocolElements.JOIN_ROOM_ROOM_PARAM);
        log.info("user joins room {}",roomName);
        extendedRoomManager.addExtRoomIfNotExists(roomName);
        notificationService.addNewParticipantSession(transaction.getSession(), participantRequest.getParticipantId());
        super.joinRoom(transaction, request, participantRequest);
        notificationService.notifyAllActiveSessions(USERS_LIST_JPARAM, extendedRoomManager.getRoomInfoList());
    }

    @Override
    public void publishVideo(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest){
        log.info("try to publish video..");
        int numPublishers;
        try {
            numPublishers = roomManager.getPeerPublishers(participantRequest.getParticipantId()).size();
        } catch (Exception ex) {
            log.error("error getting num publishers",ex);
            throw new RoomException(RoomException.Code.GENERIC_ERROR_CODE, "failed to get peer publishers");
        }
        if(numPublishers > 0){
           throw new RoomException(RoomException.Code.EXISTING_USER_IN_ROOM_ERROR_CODE, "In the room  there is already an active publisher");
        }
        super.publishVideo(transaction, request, participantRequest); 
    }
    

    @Override
    public void customRequest(Transaction transaction,
            Request<JsonObject> request, ParticipantRequest participantRequest) {
        try {
            if (request.getParams() == null
                    || request.getParams().get(SUB_METHOD_JPARAM) == null) {
                throw new RuntimeException(SUB_METHOD_JPARAM + " parameter is missing.");
            }
            switch (request.getParams().get(SUB_METHOD_JPARAM).getAsString()) {
                case WsSubEndpoints.TEST:
                    test(transaction, request, participantRequest);
                    break;
                case WsSubEndpoints.ROOMS_LIST:
                    getRooms(transaction, request, participantRequest);
                    break;
            }
        } catch (Exception e) {
            log.error("Unable to handle custom request with submethod "+request.getParams().get(SUB_METHOD_JPARAM).getAsString(), e);
            try {
                transaction.sendError(e);
            } catch (IOException e1) {
                log.error("Unable to send error response", e1);
            }
        }

    }

    public void getRooms(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest) throws Exception {
        transaction.sendResponse(extendedRoomManager.getRoomInfoList());
    }

    public void test(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest) throws Exception {
        transaction.sendResponse("It works!");
    }

}
