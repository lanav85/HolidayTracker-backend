package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
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

    @GetMapping("/RetrieveUserbyID/{userId}")
    public User getUserById(@PathVariable("userId") int userId) throws SQLException {
        return userDaoImpl.get(userId);
    }

    @GetMapping("/login/{email}")
    public User getUserByEmail(@PathVariable("email") String email) throws SQLException {
        return userDaoImpl.getUserByEmail(email);
    }

    @GetMapping("/RetrieveAllUsers")
    public List<User> getAll() throws SQLException {
        return userDaoImpl.getAll();
    }

    @PostMapping("/CreateNewUser")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
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
    }

    @PutMapping("/UpdateUser/{userId}")
    public int updateUser(@PathVariable int userId, @RequestBody User user) throws SQLException {
        user.setUserID(userId);
        return userDaoImpl.update(user);
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
