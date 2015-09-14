package com.tilab.ca.sda_kurento_demo_app;

import com.tilab.ca.sda_kurento_demo_app.data.RoomInfo;
import com.tilab.ca.sda_kurento_demo_app.internal.ExtendedRoomManager;
import java.util.List;
import java.util.stream.Collectors;
import org.kurento.room.RoomManager;
import org.kurento.room.exception.AdminException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KSdaDemoController {
    
    private static final org.slf4j.Logger log = LoggerFactory
			.getLogger(KSdaDemoController.class);
    
    @Autowired
    private RoomManager roomManager;

    @RequestMapping("/test")
    public String test() {
        return "It works!";
    }
    
    @RequestMapping("/rooms")
    public List<RoomInfo> getRooms() {
        return ((ExtendedRoomManager)roomManager).getRoomInfoList();
    }
    
    @RequestMapping("/rooms/{roomName}/thumb")
    //POST continuare da qua
    public void createRoomThumb() {
        
    }

    @RequestMapping("/getClientConfig")
    public ClientConfig clientConfig() {
        ClientConfig config = new ClientConfig();
        config.setLoopbackRemote(false);
        config.setLoopbackAndLocal(false);
        return config;
    }
}
