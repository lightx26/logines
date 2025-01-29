package com.pet.logines;

import com.pet.logines.configurations.properties.GoogleOAuthConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GoogleOAuthConfig.class)
public class LoginesApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginesApplication.class, args);
    }
}
