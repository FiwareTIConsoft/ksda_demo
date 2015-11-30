package com.tilab.ca.sda_kurento_demo_app;

import com.tilab.ca.sda_kurento_demo_app.internal.SdaDataRetrievalTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulingConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar str) {
        str.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }
    
    @Bean
    public SdaDataRetrievalTask sdaDataRetrievalTask(){
        return new SdaDataRetrievalTask();
    }
}
