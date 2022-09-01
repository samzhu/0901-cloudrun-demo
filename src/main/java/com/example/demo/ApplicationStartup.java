package com.example.demo;


import java.time.Instant;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartup {

    

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() throws Exception {
        log.info("服務啟動");
        this.save();
        this.show();
    }

    private void show(){

    }

    private void save(){

    }

}
