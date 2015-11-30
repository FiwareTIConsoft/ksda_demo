package com.tilab.ca.sda_kurento_demo_app;

import com.tilab.ca.sda_kurento_demo_app.data.RoomInfo;
import com.tilab.ca.sda_kurento_demo_app.exception.InternalErrorCode;
import com.tilab.ca.sda_kurento_demo_app.exception.KSdaDemoException;
import com.tilab.ca.sda_kurento_demo_app.internal.ExtendedRoomManager;
import java.io.FileOutputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private ExtendedRoomManager extendedRoomManager;
      
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
        return extendedRoomManager.getRoomInfoList();
    }
    
    @RequestMapping(value="/rooms/{roomName}/thumb", method=RequestMethod.POST)
    public RoomInfo createRoomThumb(@PathVariable("roomName") String roomName,
                                @RequestParam(value="ext",required = false) String imageExtension,
                                @RequestParam("thumb") MultipartFile file) {
   
       log.info("called createRoomThumb with roomName {}",roomName);
       if(!extendedRoomManager.roomExists(roomName)){
           throw new KSdaDemoException(InternalErrorCode.FAILED_TO_FIND_ROOM);
       }
       
       if (file.isEmpty()) {
           throw new KSdaDemoException(InternalErrorCode.FILE_IS_NULL_OR_EMPTY);
       }
       
       String imageName = String.format("%s_%s",roomName.replace("#", ""), System.currentTimeMillis());
       log.info("uploading file with name "+file.getName());
       String imageExt = imageExtension!=null?imageExtension:defaultImageExtension;
       RoomInfo roomInfo = null;
        try {
            String imagePath = createNewImageAndGetPath(file.getBytes(), imageName, imageExt);
            extendedRoomManager.setRoomThumbUrl(roomName, imagePath);
            roomInfo = extendedRoomManager.getRoomInfo(roomName);
        } catch (Exception ex) {
            throw new KSdaDemoException(InternalErrorCode.FAILED_TO_SAVE_FILE, ex);
        }
        
       return roomInfo;
    }

    @RequestMapping("/getClientConfig")
    public ClientConfig clientConfig() {
        ClientConfig config = new ClientConfig();
        config.setLoopbackRemote(false);
        config.setLoopbackAndLocal(false);
        return config;
    }
    
    
    private String createNewImageAndGetPath(byte[] imageBytesArray, String imageName, String imageExtension) throws Exception {        
        
        String imageInternalPath = imagesSavePath + imageName + "." + imageExtension;
        String imageExtPath = imagesPath + imageName + "." + imageExtension;
        log.debug("saving image on path "+imageInternalPath);
        try(FileOutputStream fos = new FileOutputStream(imageInternalPath)){
            fos.write(imageBytesArray);
        }
        return imageExtPath;
    }
    
    @ModelAttribute
    public void setCorsResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }    
}
