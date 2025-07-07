package com.example.common;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CommonApplication.class);
      
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}