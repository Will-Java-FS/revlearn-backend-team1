package com.revlearn.team1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testJdbcConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/project2";
        String user = "postgres";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}