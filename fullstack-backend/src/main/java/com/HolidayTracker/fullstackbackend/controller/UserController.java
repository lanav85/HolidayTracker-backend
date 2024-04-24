package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import com.HolidayTracker.fullstackbackend.service.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserDao userDaoImpl;
    @Autowired
    private UserValidator userValidator;

    @GetMapping("/RetrieveUserbyID/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") int userId) {
        try {
            User user = userDaoImpl.get(userId);
            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/login/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable("email") String email) {
        try {
            User user = userDaoImpl.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveAllUsers")
    public ResponseEntity<Object> getAll() {
        try {
            List<User> users = userDaoImpl.getAll();
            return ResponseEntity.ok(users);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/CreateNewUser")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        try {
            ResponseEntity<Object> validationResponse = userValidator.validateUserRole(newUser);
            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                return validationResponse;
            }
            try {
                int result = userDaoImpl.insert(newUser);
                if (result > 0) {
                    return new ResponseEntity<>("User successfully created", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Unable to create user", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/UpdateUser/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable int userId, @RequestBody User user) {
        try {
            ResponseEntity<Object> validationResponse = userValidator.validateUserRole(user);
            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                return validationResponse;
            }

            user.setUserID(userId);
            int result = userDaoImpl.update(user);
            if (result > 0) {
                return ResponseEntity.ok("User successfully updated");
            } else {
                return ResponseEntity.badRequest().body("Unable to update user");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/DeleteUser/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int userId) throws SQLException {

        User user = userDaoImpl.get(userId);

        try {
            int result = userDaoImpl.delete(userId);
            if (result > 0) {
                return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete user", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
/*
 * if (user.getUserType().equals("HRManager")) {
 * System.out.println("The HRManager cannot be deleted from the system");
 * return 0;
 * }
 * if (user.getUserType().equals("Manager")) {
 * List<User> users = userDaoImpl.getAllUsersByDepartmentID(id);
 *
 * if (!users.isEmpty()) {
 * System.out.println("User with ID " + id +
 * " cannot be deleted as there are users assigned to them");
 * for (User checkuser : users) {
 * System.out.println(checkuser.getEmail());
 * }
 * return 0;
 *
 * }
 * }
 * return userDaoImpl.delete(userId);
 * }
 */
