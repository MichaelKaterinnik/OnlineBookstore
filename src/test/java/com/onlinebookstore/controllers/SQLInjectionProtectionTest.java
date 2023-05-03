package com.onlinebookstore.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class SQLInjectionProtectionTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void testSqlInjectionProtection() {
        String unsafeString = "' OR 1=1 --";
        String sqlQuery = "SELECT * FROM users WHERE username = ?";

        try {
            jdbcTemplate.query(sqlQuery, new Object[]{unsafeString}, rs -> {});
            fail("Expected DataAccessException due to SQL injection vulnerability");
        } catch (DataAccessException e) {
            assertTrue(e.getMessage().contains("PreparedStatementCallback"), "Exception should be due to a SQL injection vulnerability");
        }
    }

}
