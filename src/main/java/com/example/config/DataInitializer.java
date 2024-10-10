package com.example.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Data initializer class for initializing the database.
 */
@Log4j2
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Initializes the database with the users.sql file.
     *
     * @param args the command line arguments
     * @throws Exception if an error occurs
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("run called");

        String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/sql/users.sql")));

        jdbcTemplate.execute(sql);
    }
}