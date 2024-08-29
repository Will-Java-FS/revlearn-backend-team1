package com.revlearn.team1.unit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testJdbcConnection {
    public static void main(String[] args) {
        String url = "${SPRING_DATASOURCE_URL}";
        String user = "${POSTGRES_USER}";
        String password = "${POSTGRES_PASSWORD}";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
