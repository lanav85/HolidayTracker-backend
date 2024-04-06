package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class RoleController {
    @Autowired
    private RoleDao RoleDaoImpl;

    @GetMapping("/RetrieveRolebyID/{userId}")
    public User getRoleById(@PathVariable("userId") int userId) throws SQLException {
        return RoleDaoImpl.get(userId);
    }


    @GetMapping("/RetrieveAllRoles")
    public List<Role> getAll() throws SQLException {
        return RoleDaoImpl.getAll();
    }

    @PostMapping("/CreateNewRole")
    public ResponseEntity<Object> createRole(@RequestBody Role newRole) {
        try {
            int result = RoleDaoImpl.insert(newRole);
            if (result > 0) {
                return new ResponseEntity<>("Role successfully created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to create role", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/UpdateRole/{roleId}")
    public int updateRole(@PathVariable int roleId, @RequestBody User user) throws SQLException {
        role.setRoleID(roleId);
        return RoleDaoImpl.update(role);
    }

    @DeleteMapping("/DeleteRole/{roleId}")
    public ResponseEntity<Object> deleteRoleById(@PathVariable int roleId) throws SQLException {

        Role role = RoleDaoImpl.get(roleId);

        try {
            int result = RoleDaoImpl.delete(roleId);
            if (result > 0) {
                return new ResponseEntity<>("Role successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete role", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
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
        }
        return userDaoImpl.delete(userId);
    }*/
