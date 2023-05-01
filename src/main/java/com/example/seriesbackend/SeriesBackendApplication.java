package com.example.seriesbackend;

import com.example.seriesbackend.service.impl.MegogoPageParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SeriesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeriesBackendApplication.class, args);
    }

}
