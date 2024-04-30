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

    //method that allows only one manager by department
    public ResponseEntity<Object> validateUserRole(User newUser) {
        try {
            int departmentID = newUser.getDepartmentID();
            int roleID = newUser.getRoleID();
            String departmentName = getDepartmentNameById(departmentID);
            String userName = extractUserName(newUser.getData());

            // Check if the user is assigned roleID 2
            if (roleID == 2) {
                // Retrieve all users of the same department
                List<User> usersInDepartment = userDao.getAllUsersByDepartmentID(departmentID);

                // Check if there's already a user with roleID 2 in the department
                for (User user : usersInDepartment) {
                    if (user.getRoleID() == 2 && user.getUserID() != newUser.getUserID()) { //checked as well if the userID isn't the same of anyone in the list in case of update requests.
                        // If a user with roleID 2 already exists, print error message

                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user " + userName + " is already the manager of the department of " + departmentName + ". Please update role.");
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

    private String extractUserName(String userData) {
        // Initialize ObjectMapper to parse JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert JSON string to JsonNode
            JsonNode node = mapper.readTree(userData);
            // Extract and return the value of the "name" field
            return node.get("name").asText();
        } catch (IOException e) {
            System.err.println("IOException occurred while parsing user data: " + e.getMessage());
            return "";
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

    // Method to update UserID in Department table
    private void updateDepartmentUserID(int departmentID, int userID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "UPDATE Department SET UserID = ? WHERE DepartmentID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setInt(2, departmentID);
        ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
    }

    // Method to create or update user and update Department UserID if necessary
    public ResponseEntity<Object> createUserOrUpdateUser(User user) {
        try {
            int result;
            if (user.getUserID() != 0) {
                // Update existing user
                result = userDao.update(user);
            } else {
                // Create new user
                result = userDao.insert(user);
            }

            if (result > 0 && user.getRoleID() == 2) {
                // Update Department UserID if the user has roleID 2
                updateDepartmentUserID(user.getDepartmentID(), user.getUserID());
            }

            return ResponseEntity.ok("User created/updated successfully.");
        } catch (SQLException e) {
            System.err.println("SQLException occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQLException occurred: " + e.getMessage());
        }
    }
}
