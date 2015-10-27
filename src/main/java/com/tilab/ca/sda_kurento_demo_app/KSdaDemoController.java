package com.tilab.ca.sda_kurento_demo_app;

import com.tilab.ca.sda_kurento_demo_app.data.RoomInfo;
import com.tilab.ca.sda_kurento_demo_app.exception.InternalErrorCode;
import com.tilab.ca.sda_kurento_demo_app.exception.KSdaDemoException;
import com.tilab.ca.sda_kurento_demo_app.internal.ExtendedRoomManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.kurento.room.RoomManager;
import org.kurento.room.exception.AdminException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class KSdaDemoController {
    
    private static final org.slf4j.Logger log = LoggerFactory
			.getLogger(KSdaDemoController.class);
    
    @Autowired
    private RoomManager roomManager;
      
    @Value("${images.path:./}")
    private String imagesPath;

    @Value("${images.extension:png}")
    private String defaultImageExtension;
    
    @Value("${images.save.path:./}")
    private String imagesSavePath;

    @RequestMapping("/test")
    public String test() {
        return "It works!";
    }
    
    @RequestMapping("/rooms")
    public List<RoomInfo> getRooms() {
        return ((ExtendedRoomManager)roomManager).getRoomInfoList();
    }
    
    @RequestMapping(value="/rooms/{roomName}/thumb", method=RequestMethod.POST)
    public void createRoomThumb(@PathVariable("roomName") String roomName,
                                @RequestParam("ext") String imageExtension,
                                @RequestParam("thumb") MultipartFile file) {
       ExtendedRoomManager extendedRoomManager = ((ExtendedRoomManager)roomManager);
       if(!extendedRoomManager.getRooms().contains(roomName)){
           throw new KSdaDemoException(InternalErrorCode.FAILED_TO_FIND_ROOM);
       }
       if (file.isEmpty()) {
           throw new KSdaDemoException(InternalErrorCode.FILE_IS_NULL_OR_EMPTY);
       }
       
       String imageName = String.format("%s_%s",roomName.replace("#", ""), System.currentTimeMillis());
       String imageExt = imageExtension!=null?imageExtension:defaultImageExtension;
       
        try {
            String imagePath = createNewImageAndGetPath(file.getBytes(), imageName, imageExt);
            extendedRoomManager.setRoomThumbUrl(roomName, imagePath);
        } catch (Exception ex) {
            throw new KSdaDemoException(InternalErrorCode.FAILED_TO_SAVE_FILE, ex);
        }
       
    }

    @RequestMapping("/getClientConfig")
    public ClientConfig clientConfig() {
        ClientConfig config = new ClientConfig();
        config.setLoopbackRemote(false);
        config.setLoopbackAndLocal(false);
        return config;
    }
    
    
    private String createNewImageAndGetPath(byte[] imageBytesArray, String imageName, String imageExtension) throws Exception {        
        
        String imagePath = imagesPath + imageName + "." + imageExtension;
        log.debug("saving image on path "+imagePath);
        try(FileOutputStream fos = new FileOutputStream(imagePath)){
            fos.write(imageBytesArray);
        }
        return imagePath;
    }
}
