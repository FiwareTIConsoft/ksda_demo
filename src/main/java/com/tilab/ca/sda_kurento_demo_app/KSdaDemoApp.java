package com.tilab.ca.sda_kurento_demo_app;

import com.google.gson.JsonArray;
import java.util.List;
import static org.kurento.commons.PropertiesManager.getPropertyJson;
import org.kurento.jsonrpc.JsonUtils;
import org.kurento.room.KurentoRoomServerApp;
import org.kurento.room.RoomJsonRpcHandler;
import org.kurento.room.kms.FixedOneKmsManager;
import org.kurento.room.kms.KmsManager;
import org.kurento.room.rpc.JsonRpcUserControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import(KurentoRoomServerApp.class)
public class KSdaDemoApp {

    private static final Logger log = LoggerFactory
            .getLogger(KSdaDemoApp.class);

    private static ConfigurableApplicationContext context;
    
    public static final String KMSS_URIS_PROPERTY = "kms.uris";
    public static final String KMSS_URIS_DEFAULT = "[ \"ws://localhost:8888/kurento\" ]";

    private static final JsonRpcUserControl ksdaJsonRpcUserControl = new KSdaJsonRpcUserControl();
    
    
    
    
    
//    @Bean
//    public TaskScheduler taskScheduler(){
//        //return (TaskScheduler) context.getBean("jsonrpcTaskScheduler");
//        return new ThreadPoolTaskScheduler();
//    }
    
    

    @Bean
    public KmsManager kmsManager() {
        JsonArray kmsUris = getPropertyJson(KMSS_URIS_PROPERTY,
                KMSS_URIS_DEFAULT, JsonArray.class);
        List<String> kmsWsUris = JsonUtils.toStringList(kmsUris);

        log.info("Configuring Kurento Room Server to use first of the following kmss: "
                + kmsWsUris);

        return new FixedOneKmsManager(kmsWsUris.get(0));
    }

    @Bean
    public JsonRpcUserControl userControl() {
        return ksdaJsonRpcUserControl;
    }
    
    @Bean
    public RoomJsonRpcHandler roomHandler() {
        return new KSdaRoomJsonRpcHandler();
    }


    public static ConfigurableApplicationContext start(Object... sources) {

        Object[] newSources = new Object[sources.length + 1];
        newSources[0] = KurentoRoomServerApp.class;
        for (int i = 0; i < sources.length; i++) {
            newSources[i + 1] = sources[i];
        }

        SpringApplication application = new SpringApplication(newSources);
        context = application.run();
        return context;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(KSdaDemoApp.class, args);
    }

    public static void stop() {
        context.stop();
    }

}
