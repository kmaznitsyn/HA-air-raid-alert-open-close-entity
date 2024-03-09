package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AppApplication {

    private static HAScheduler scheduler;

    @Autowired
    public AppApplication(HAScheduler scheduler) {
        AppApplication.scheduler = scheduler;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(scheduler, 0, 10, TimeUnit.SECONDS);
    }

}
