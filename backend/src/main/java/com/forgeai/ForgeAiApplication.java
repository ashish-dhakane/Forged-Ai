package com.forgeai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main entry point for the ForgeAI Spring Boot application.
@SpringBootApplication
public class ForgeAiApplication {

    // Starts the Spring Boot container and initializes all application beans.
    public static void main(String[] args) {
        SpringApplication.run(ForgeAiApplication.class, args);
    }
}
