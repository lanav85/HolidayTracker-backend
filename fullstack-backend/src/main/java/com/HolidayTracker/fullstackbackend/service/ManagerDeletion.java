package com.HolidayTracker.fullstackbackend.service;

import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.util.List;

@Component
public class ManagerDeletion {
    private final UserDao userDao;

    @Autowired
    public ManagerDeletion(UserDao userDao) {
        this.userDao = userDao;
    }

    public ResponseEntity<Object> handleManagerDeletion(int deletedManagerId) {
        try {
            // Retrieve the department ID of the deleted manager
            User deletedManager = userDao.get(deletedManagerId);
            int managerRoleId = deletedManager.getRoleID();

            if (managerRoleId == 2) {
                int departmentId = deletedManager.getDepartmentID();

                // Retrieve all users within the deleted manager's department
                List<User> usersInDepartment = userDao.getAllUsersByDepartmentID(departmentId);

                // Retrieve the ID of the Senior Manager (role ID 3)
                int seniorManagerRoleId = 3;

                // Iterate through these users, excluding the deleted manager, and update their role to the Senior Manager
                for (User user : usersInDepartment) {
                    // Skip the deleted manager
                    if (user.getUserID() != deletedManagerId) {
                        // Update the user's role to the Senior Manager (role ID 3)
                        user.setRoleID(seniorManagerRoleId);
                        userDao.update(user);
                    }
                }

                return ResponseEntity.ok("Users within the deleted manager's department have been reassigned to the Senior Manager.");
            } else {
                return  ResponseEntity.ok().build();
            }
        } catch (SQLException e) {
            System.err.println("SQLException occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQLException occurred: " + e.getMessage());
        }
    }
}
