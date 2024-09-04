package com.revlearn.team1.unit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class testJdbcConnection {
    private static final Logger logger = LoggerFactory.getLogger(testJdbcConnection.class);
    public static void main(String[] args) {
        String url = "${SPRING_DATASOURCE_URL}";
        String user = "${POSTGRES_USER}";
        String password = "${POSTGRES_PASSWORD}";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null && conn.isValid(2)) {
                logger.info("Connection successful!");
                return;
            }
            logger.info("Connection failed or is not valid.");
        } catch (SQLException e) {
            logger.error("Connection failed: " + e.getMessage(), e);
        }
    }
}
