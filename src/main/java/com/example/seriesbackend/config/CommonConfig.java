package com.example.seriesbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class CommonConfig {

    @Bean
    ScheduledExecutorService scheduledExecutorService(){
        return Executors.newSingleThreadScheduledExecutor();
    }
}
