package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.Department;
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

    @GetMapping("/RetrieveDepartmentbyID/{departmentId}")
    public ResponseEntity<Object> getDepartmentById(@PathVariable("departmentId") int departmentId) {
        try {
            Department department = departmentDao.get(departmentId);
            if (department != null) {
                return new ResponseEntity<>(department, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/RetrieveAllDepartments")
    public ResponseEntity<Object> getAllDepartments() {
        try {
            List<Department> departments = departmentDao.getAll();
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
