package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Main class of the application. */
@SpringBootApplication
@EnableScheduling
public class ProjectApplication {
    /**
     * Main method of the application.
     *
     * @param args the arguments of the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}
