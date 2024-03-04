package com.HolidayTracker.fullstackbackend.repository;

import java.sql.*;

public class Database {
    private static String url = "jdbc:postgresql://localhost:5432/HolidayTrackerDB";
    private static String username = "postgres";
    private static String password = "1234";

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
    // Closes the connection to the database
    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    // Method to close a statement used for executing  SQL queries
    public static void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }
    // Method to close a prepared statement used for executing parameterized SQL queries
    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }
    // Method to close a result set used for retrieving data from a database query
    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }

}
