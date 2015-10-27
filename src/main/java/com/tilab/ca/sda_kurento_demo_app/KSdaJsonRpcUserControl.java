package com.tilab.ca.sda_kurento_demo_app;

import com.google.gson.JsonObject;
import com.tilab.ca.sda_kurento_demo_app.internal.ExtendedRoomManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.kurento.jsonrpc.Transaction;
import org.kurento.jsonrpc.message.Request;
import org.kurento.room.api.pojo.ParticipantRequest;
import org.kurento.room.exception.RoomException;
import org.kurento.room.rpc.JsonRpcUserControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class KSdaJsonRpcUserControl extends JsonRpcUserControl {

    private static final Logger log = LoggerFactory
            .getLogger(KSdaJsonRpcUserControl.class);

    private static final String SUB_METHOD_JPARAM = "subMethod";
    private static final String ROOM_JPARAM = "room";
    private static final String THUMB_JPARAM = "thumbBase64";

    


    @Override
    public void customRequest(Transaction transaction,
            Request<JsonObject> request, ParticipantRequest participantRequest) {
        log.debug("entro!");
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
        log.debug(roomManager.getClass().toString());
        log.debug("roommanager instance of extended "+(roomManager instanceof ExtendedRoomManager));
        transaction.sendResponse(((ExtendedRoomManager) roomManager).getRoomInfoList());
    }

    public void test(Transaction transaction, Request<JsonObject> request, ParticipantRequest participantRequest) throws Exception {
        transaction.sendResponse("It works!");
    }

}
