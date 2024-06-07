package com.HolidayTracker.fullstackbackend.service;

import com.HolidayTracker.fullstackbackend.constants.Roles;
import com.HolidayTracker.fullstackbackend.model.Department;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.model.UserWithRoleName;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.department.DepartmentDao;
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
    @Autowired
    private DepartmentDao departmentDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    // Method that allows only one manager by department
    public ResponseEntity<Object> validateUserRole(User newUser) {
        try {
            int departmentID = newUser.getDepartmentID();
            int roleID = newUser.getRoleID();
            String departmentName = getDepartmentNameById(departmentID);

            // Check if the user is assigned roleID 2
            if (roleID == 2) {
                // Retrieve all users of the same department
                List<UserWithRoleName> usersInDepartment = userDao.getAllUsersByDepartmentID(departmentID);

                // Check if there's already a user with roleID 2 in the department
                for (User user : usersInDepartment) {
                    if (user.getRoleID() == 2 && user.getUserID() != newUser.getUserID()) {
                        // Extract the username of the current manager
                        String currentManagerName = extractUserName(user.getData());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user " + currentManagerName + " is already the manager of the department of " + departmentName + ". Please update role.");
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(userData);
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

    private void updateDepartmentUserID(int departmentID, Integer userID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "UPDATE Department SET UserID = ? WHERE DepartmentID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        if (userID != null) {
            ps.setInt(1, userID);
        } else {
            ps.setNull(1, java.sql.Types.INTEGER);
        }
        ps.setInt(2, departmentID);
        ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
    }

    // Check if a department has any managers assigned to it. If there are no managers in the department, it updates the department's UserID to null.
    private void updateDepartmentUserIDtoNull(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT COUNT(*) AS count FROM Users WHERE DepartmentID = ? AND RoleID = 2";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int managerCount = rs.getInt("count");
            if (managerCount == 0) {
                // If there are no more managers in the department, set UserID to null
                updateDepartmentUserID(departmentID, null); // Pass null as the userID parameter
            }
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
    }

    public ResponseEntity<Object> createUserOrUpdateUser(User user) {
        try {
            ResponseEntity<Object> validationResponse = validateUserRole(user);
            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                return validationResponse;
            }

            int result;
            // Fetch the old user details before updating
            User oldUser = user.getUserID() != 0 ? userDao.get(user.getUserID()) : null;

            //Validate the user changes (throws an IllegalArgumentException if any violations)
            validateUser(oldUser, user);

            // If user is being updated
            if (oldUser != null) {
                // Update user details
                result = userDao.update(user);

                // If the user's department or role has changed, handle the previous and new department management logic
                if (oldUser.getRoleID() == 2 && user.getRoleID() != 2) {
                    // If the user was a manager and their role is updated to something else, update the department's UserID
                    updateDepartmentUserIDtoNull(oldUser.getDepartmentID());
                }

                if (oldUser.getDepartmentID() != user.getDepartmentID() && oldUser.getRoleID() == 2) {
                    // If the user was a manager and their department is updated, update the old department's UserID
                    updateDepartmentUserIDtoNull(oldUser.getDepartmentID());
                }
            } else {
                // If user is new
                result = userDao.insert(user);
            }

            // If the user is a manager in the new role or department, update the new department's UserID
            if (result > 0 && user.getRoleID() == 2) {
                updateDepartmentUserID(user.getDepartmentID(), user.getUserID());
            }

            return ResponseEntity.ok("User created/updated successfully.");
        } catch (SQLException e) {
            System.err.println("SQLException occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQLException occurred: " + e.getMessage());
        } catch (IllegalArgumentException e){
            System.err.println("IllegalArgumentException occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Check if this new/updated user would be valid. If not throw an IllegalArgumentException
     * @param initialUserState The previous user data before the update. For new users this will be null
     * @param newUserState The new user data requested
     */
    private void validateUser(User initialUserState, User newUserState) throws IllegalArgumentException, SQLException {

        //The role id must be validated if
        // - this is a new user (initialUserState == null) OR
        // - this users previous role id is different to their new role id
        boolean hasNewRoleId = initialUserState == null || (initialUserState.getRoleID() != newUserState.getRoleID());

        if(hasNewRoleId){
            if(newUserState.getRoleID() == Roles.Manager.getRoleId()){
                //If this new user will be a manager, we need to make sure that department doesn't already have a manager
                Department department = departmentDao.get(newUserState.getDepartmentID());
                if(department.getUserID() != null && department.getUserID() != newUserState.getUserID()){
                    //There is already a manager for this department and its not this newUser, therefore a violation
                    throw new IllegalArgumentException(department.getDepartmentName() + " department already has a different manager assigned!");
                }
            }
        }
    }

}
