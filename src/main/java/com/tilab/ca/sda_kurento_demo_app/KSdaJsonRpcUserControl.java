package com.tilab.ca.sda_kurento_demo_app;

import com.google.gson.JsonObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.tilab.ca.sda_kurento_demo_app.internal.ExtendedRoomManager;
import com.tilab.ca.sda_kurento_demo_app.utils.se.WsSubEndpoints;
import com.tilab.ca.sda_kurento_demo_app.utils.se.WsSubEndpointsManager;
import com.tilab.ca.sda_kurento_demo_app.utils.se.WsSubEndpointsParams;
import java.io.FileOutputStream;
import java.io.IOException;
import org.kurento.jsonrpc.Transaction;
import org.kurento.jsonrpc.message.Request;
import org.kurento.room.api.pojo.ParticipantRequest;
import org.kurento.room.exception.RoomException;
import org.kurento.room.rpc.JsonRpcUserControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class KSdaJsonRpcUserControl extends JsonRpcUserControl {

    @Autowired
    private WsSubEndpointsManager<WsSubEndpointsParams> wsSubEndpointsManager;

    private static final Logger log = LoggerFactory
            .getLogger(KSdaJsonRpcUserControl.class);

    private static final String SUB_METHOD_JPARAM = "subMethod";
    private static final String ROOM_JPARAM = "room";
    private static final String THUMB_JPARAM = "thumbBase64";
    
    @Value("#{images.path}")
    private String imagesPath;
    
    @Value("#{images.extension}")
    private String defaultImageExtension;

    public KSdaJsonRpcUserControl() {

    }

    private void registerHandlers() {
        //sub endpoints registration
        wsSubEndpointsManager.registerHandler(WsSubEndpoints.TEST, params -> test(params))
                .registerHandler(WsSubEndpoints.ROOMS_LIST, params -> getRooms(params))
                .registerHandler(WsSubEndpoints.SET_ROOM_THUMB, params -> setRoomThumb(params));
    }

    @Override
    public void customRequest(Transaction transaction,
            Request<JsonObject> request, ParticipantRequest participantRequest) {
        try {
            if (request.getParams() == null
                    || request.getParams().get(SUB_METHOD_JPARAM) == null) {
                throw new RuntimeException(SUB_METHOD_JPARAM + " parameter is missing.");
            }

            wsSubEndpointsManager.handleRequest(request.getParams().get(SUB_METHOD_JPARAM).getAsString(), new WsSubEndpointsParams(transaction, request, participantRequest));
        } catch (Exception e) {
            log.error("Unable to handle custom request", e);
            try {
                transaction.sendError(e);
            } catch (IOException e1) {
                log.error("Unable to send error response", e1);
            }
        }

    }

    public void getRooms(WsSubEndpointsParams params) throws Exception {
        params.getTransaction().sendResponse(((ExtendedRoomManager) roomManager).getRoomInfoList());
    }

    public void test(WsSubEndpointsParams params) throws Exception {
        params.getTransaction().sendResponse("It works!");
    }

    public void setRoomThumb(WsSubEndpointsParams params) throws Exception {
        String roomName = params.getRequest().getParams().get(ROOM_JPARAM).getAsString();
        if (!((ExtendedRoomManager) roomManager).roomExists(roomName)) {
            throw new RoomException(RoomException.Code.ROOM_NOT_FOUND_ERROR_CODE, String.format("room %s does not exists", roomName));
        }
        String thumbBase64 = params.getRequest().getParams().get(THUMB_JPARAM).getAsString();
        ((ExtendedRoomManager) roomManager).setRoomThumbUrl(roomName, createNewImageAndGetPath(thumbBase64, roomName, defaultImageExtension));
        
    }

    private String createNewImageAndGetPath(String thumbBase64, String imageName,String imageExtension) throws Exception{
        byte[] imageBytesArray = Base64.decode(thumbBase64);
        String imagePath = imagesPath+imageName+"."+imageExtension;
        FileOutputStream fos = new FileOutputStream(imagePath);
        fos.write(imageBytesArray);
        fos.close();
        return imagePath;
    }

}
