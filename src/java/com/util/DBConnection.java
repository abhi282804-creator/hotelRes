package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String DEFAULT_DB_USER = "root";

    private static String getEnvOrProperty(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(name);
        }
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        String dbUrl = getEnvOrProperty("HOTEL_DB_URL");
        if (dbUrl == null) {
            dbUrl = DEFAULT_DB_URL;
        }

        String dbUser = getEnvOrProperty("HOTEL_DB_USER");
        if (dbUser == null) {
            dbUser = DEFAULT_DB_USER;
        }

        String dbPassword = getEnvOrProperty("HOTEL_DB_PASSWORD");
        if (dbPassword == null) {
            throw new SQLException("Database configuration error: Missing required environment variable 'HOTEL_DB_PASSWORD'.");
        }

        Connection connection = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // Open a connection
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            
            if (connection != null) {
                System.out.println("Successfully connected to the database.");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            throw e;
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            throw e;
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}