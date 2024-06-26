package com.HolidayTracker.fullstackbackend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class Database {
    private final String url;
    private final String username;
    private final String password;

    private static Database instance;

    @Autowired
    public Database(@Value("${spring.datasource.url}") String url,
                    @Value("${spring.datasource.username}") String username,
                    @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        instance = this;
    }

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(instance.url, instance.username, instance.password);
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
