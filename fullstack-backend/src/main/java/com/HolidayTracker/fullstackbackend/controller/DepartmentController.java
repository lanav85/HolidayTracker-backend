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
public class DepartmentController {
    @Autowired
    private DepartmentDao DepartmentDaoImpl;

    @GetMapping("/RetrieveDepartmentbyID/{departmentId}")
    public User getDepartmentById(@PathVariable("departmentId") int departmentId) throws SQLException {
        return DepartmentDaoImpl.get(departmentId);
    }


    @GetMapping("/RetrieveAllDepartments")
    public List<Department> getAll() throws SQLException {
        return DepartmentDaoImpl.getAll();
    }

    @PostMapping("/CreateNewDepartment")
    public ResponseEntity<Object> createDepartment(@RequestBody Department newDepartment) {
        try {
            int result = DepartmentDaoImpl.insert(newDepartment);
            if (result > 0) {
                return new ResponseEntity<>("Department successfully created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to create department", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/UpdateDepartment/{departmentId}")
    public int updateDepartment(@PathVariable int departmentId, @RequestBody Department department) throws SQLException {
        department.setDepartmentID(departmentId);
        return DepartmentDaoImpl.update(department);
    }

    @DeleteMapping("/DeleteDepartment/{departmentId}")
    public ResponseEntity<Object> deleteDepartmentById(@PathVariable int departmentId) throws SQLException {

        Department department = DepartmentDaoImpl.get(departmentId);

        try {
            int result = DepartmentDaoImpl.delete(departmentId);
            if (result > 0) {
                return new ResponseEntity<>("Department successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete department", HttpStatus.BAD_REQUEST);
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
