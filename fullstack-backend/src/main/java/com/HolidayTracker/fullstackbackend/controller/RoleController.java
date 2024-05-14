package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.Role;
import com.HolidayTracker.fullstackbackend.repository.role.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleDao roleDao;

    @GetMapping
    public ResponseEntity<Object> getRole(
            @RequestParam(required = false) Integer roleId) {
        try {
            if (roleId != null) {
                List<Role> roles = roleDao.getRoleByRoleID(roleId);
                if (roles != null) {
                    return ResponseEntity.ok(roles);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Role with ID " + roleId + " not found");
                }
            } else {
                List<Role> allRoles = roleDao.getAll();
                return ResponseEntity.ok(allRoles);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving roles: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveAllRoles")
    public List<Role> getAllRoles() throws SQLException {
        return roleDao.getAll();
    }

    @PostMapping("/CreateNewRole")
    public ResponseEntity<Object> createRole(@RequestBody Role newRole) {
        try {
            int result = roleDao.insert(newRole);
            if (result > 0) {
                return new ResponseEntity<>("Role successfully created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to create role", HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/UpdateRole/{roleId}")
    public ResponseEntity<Object> updateRole(@PathVariable int roleId, @RequestBody Role role) {
        try {
            role.setRoleID(roleId);
            int result = roleDao.update(role);
            if (result > 0) {
                return new ResponseEntity<>("Role successfully updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to update role", HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/DeleteRole/{roleId}")
    public ResponseEntity<Object> deleteRoleById(@PathVariable int roleId) {
        try {
            int result = roleDao.delete(roleId);
            if (result > 0) {
                return new ResponseEntity<>("Role successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete role", HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
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
