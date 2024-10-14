package com.example.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** Data initializer class for initializing the database. */
@Log4j2
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Run method for initializing the database.
     *
     * @param args Command line arguments
     * @throws Exception Exception thrown if an error occurs
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("run called");

        executeSql("src/main/resources/sql/init.sql");
    }

    /**
     * Execute SQL method for executing SQL statements.
     *
     * @param filePath File path of the SQL file
     * @throws IOException Exception thrown if an error occurs
     */
    private void executeSql(String filePath) throws IOException {
        log.info("executeSql called");

        String sql = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] sqlStatements = sql.split(";");

        for (String statement : sqlStatements) {
            jdbcTemplate.execute(statement.trim());
        }
    }
}
