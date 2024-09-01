package ru.anyline.urlcut.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class ListenerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerConfig.class);

    @EventListener(classes = {ContextRefreshedEvent.class, ContextStartedEvent.class})
    public void onContextStarted(){
        LOGGER.info("app started on port 8080, redis on port 6379");
    }
}
