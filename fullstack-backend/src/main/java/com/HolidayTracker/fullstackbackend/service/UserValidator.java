package com.HolidayTracker.fullstackbackend.service;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserValidator {
    @Autowired
    private final UserDao userDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    //Service layer to allow only one manager by department
    public ResponseEntity<Object> validateUserRole(User newUser) {
        try {
            int departmentID = newUser.getDepartmentID();
            int roleID = newUser.getRoleID();

            // Check if the user is assigned roleID 2
            if (roleID == 2) {
                // Retrieve all users of the same department
                List<User> usersInDepartment = userDao.getAllUsersByDepartmentID(departmentID);

                // Check if there's already a user with roleID 2 in the department
                for (User user : usersInDepartment) {
                    if (user.getRoleID() == 2) {
                        // If a user with roleID 2 already exists, print error message
                        String departmentName = getDepartmentNameById(departmentID);
                        String userName = extractUserName(user.getData());
                        String errorMessage = "The user " + userName + " is already the manager of the department of " + departmentName + ". Please update role.";
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQLException occurred: " + e.getMessage());
        }

        // If no user with roleID 2 exists in the department, return success message
        return ResponseEntity.ok("User role validation successful");
    }
    // method that extracts the user's name from the JSON data.
    private String extractUserName(String userData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(userData);
            return node.get("name").asText();
        } catch (IOException e) {
            // Handle IOException
            System.err.println("IOException occurred while parsing user data: " + e.getMessage());
            return ""; // or throw custom exception
        }
    }
    public String getDepartmentNameById(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String departmentName = null;
        String sql = "SELECT DepartmentName FROM Department WHERE DepartmentID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            departmentName = rs.getString("DepartmentName");
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return departmentName;
    }
}
