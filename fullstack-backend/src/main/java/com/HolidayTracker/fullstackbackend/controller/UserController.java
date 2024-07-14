package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.auth.AuthenticationHelper;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.model.UserLogin;
import com.HolidayTracker.fullstackbackend.model.UserWithRoleName;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import com.HolidayTracker.fullstackbackend.service.UserValidator;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserDao userDaoImpl;
    @Autowired
    private UserValidator userValidator;

    @Value("${aws.iam.clientid}")
    private String awsClientId;
    @Value("${aws.cognito.userpoolid}")
    private String awsUserPoolId;
    @Value("${aws.cognito.region}")
    private String awsRegion;

    @PostMapping("/login")
    public ResponseEntity<Object> getUsers(
            @RequestBody UserLogin userLogin){

        AuthenticationHelper authenticationHelper = new AuthenticationHelper(
                awsUserPoolId,
                awsClientId,
                awsRegion
        );

        JSONObject awsCognitoResponse;
        try {
            awsCognitoResponse = authenticationHelper.PerformSRPAuthentication(userLogin.getEmail(), userLogin.getPassword());
        }
        catch (NotAuthorizedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getErrorMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        String verifiedEmail = awsCognitoResponse.getString("email");

        ResponseEntity<Object> userResponse = getUsers(null, verifiedEmail, null);
        if(userResponse.getStatusCode().is2xxSuccessful()){
            try {
                return ResponseEntity.ok((User) userResponse.getBody());
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is registered but error getting user data: " + e.getMessage());
            }
        }else {
            return userResponse;
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer departmentId) {
        try {
            if (userId != null) {
                User user = userDaoImpl.get(userId);
                if (user != null) {
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + userId + " not found");
                }
            } else if (email != null) {
                User user = userDaoImpl.getUserByEmail(email);
                if (user != null) {
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found");
                }
            } else if (departmentId != null) {
                List<UserWithRoleName> users = userDaoImpl.getAllUsersByDepartmentID(departmentId);
                if (users != null) {
                    return ResponseEntity.ok(users);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users with departmentId " + departmentId + " not found");
                }
            } else {
                List<User> users = userDaoImpl.getAll();
                return ResponseEntity.ok(users);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/")
    public ResponseEntity getHealth(){
        return ResponseEntity.status(HttpStatus.OK).body("Status: online");
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        return userValidator.createUserOrUpdateUser(newUser);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable int userId, @RequestBody User user) {
        user.setUserID(userId);
        return userValidator.createUserOrUpdateUser(user);
    }

    @DeleteMapping("/users/{userId}")
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