package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.user.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserDaoImpl userDaoImpl;

    @GetMapping("/RetrieveUserbyID/{userId}")
    public User getUserById(@PathVariable("userId") int userId) throws SQLException {
        return userDaoImpl.get(userId); // Assuming userDaoImpl is your DAO implementation
    }


    @GetMapping("/RetrieveAllUsers")
    public List<User> getAll() throws SQLException {
        return userDaoImpl.getAll();
    }

    @PostMapping("/CreateNewUser")
    public int createUser(@RequestBody User newUser) throws SQLException {
        return userDaoImpl.insert(newUser);
    }

    @PutMapping("/UpdateUser")
    public int updateUser(@PathVariable int id, @RequestBody User user) throws SQLException {
        user.setUserID(id);
        return userDaoImpl.update(user);
    }

    @DeleteMapping("/DeleteUser")
    public int deleteUserById(@PathVariable int id) throws SQLException {
        User user = userDaoImpl.get(id);
        if (user == null) {
            System.out.println("User with ID " + id + " does not exist");
            return 0;
        }
        /*if (user.getUserType().equals("HRManager")) {
            System.out.println("The HRManager cannot be deleted from the system");
            return 0;
        }
        if (user.getUserType().equals("Manager")) {
            List<User> users = userDaoImpl.getAllUsersByDepartmentID(id);

            if (!users.isEmpty()) {
                System.out.println("User with ID " + id + " cannot be deleted as there are users assigned to them");
                for (User checkuser : users) {
                    System.out.println(checkuser.getEmail());
                }
                return 0;

            }
        } */
        return userDaoImpl.delete(id);
    }
}