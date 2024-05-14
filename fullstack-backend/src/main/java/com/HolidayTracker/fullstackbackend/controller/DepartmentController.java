package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.Department;
import com.HolidayTracker.fullstackbackend.model.DepartmentWithUserName;
import com.HolidayTracker.fullstackbackend.model.Role;
import com.HolidayTracker.fullstackbackend.repository.department.DepartmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentDao departmentDao;

    @GetMapping("/Department")
    public ResponseEntity<Object> getDepartment(@RequestParam(required = false) Integer departmentID) {
        try {
            if (departmentID != null) {
                List<DepartmentWithUserName> department = departmentDao.getDepartmentsByDepartmentID(departmentID);
                if (!department.isEmpty()) {
                    return ResponseEntity.ok(department);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Department with ID " + departmentID + " not found");
                }
            } else {
                List<Department> allDepartments = departmentDao.getAll();
                return ResponseEntity.ok(allDepartments);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving department: " + e.getMessage());
        }
    }
    @PostMapping("/CreateNewDepartment")
    public ResponseEntity<Object> createDepartment(@RequestBody Department newDepartment) {
        try {
            int result = departmentDao.insert(newDepartment);
            if (result > 0) {
                return new ResponseEntity<>("Department successfully created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to create department", HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/UpdateDepartment/{departmentId}")
    public ResponseEntity<Object> updateDepartment(@PathVariable int departmentId, @RequestBody Department department) {
        department.setDepartmentID(departmentId);
        try {
            int result = departmentDao.update(department);
            if (result > 0) {
                return new ResponseEntity<>("Department successfully updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to update department", HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/DeleteDepartment/{departmentId}")
    public ResponseEntity<Object> deleteDepartmentById(@PathVariable int departmentId) {
        try {
            int result = departmentDao.delete(departmentId);
            if (result > 0) {
                return new ResponseEntity<>("Department successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete department", HttpStatus.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
