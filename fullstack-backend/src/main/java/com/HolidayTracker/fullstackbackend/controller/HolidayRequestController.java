package com.HolidayTracker.fullstackbackend.controller;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.HolidayRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@RestController
public class HolidayRequestController {
    @Autowired
    private HolidayRequestDAO holidayRequestDAO;

    @GetMapping("/RetrieveAllholidayRequestS")
    public ResponseEntity<Object> getAll() {
        try {
            List<HolidaysRequest> holidayRequests = holidayRequestDAO.getAllHolidayRequests();
            return ResponseEntity.ok().body(holidayRequests);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday requests: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveHolidayRequestbyID/{id}")
    public ResponseEntity<Object> getHolidayRequestByID(@PathVariable int id) {
        try {
            HolidaysRequest holidayRequest = holidayRequestDAO.getHolidayRequestByID(id);
            if (holidayRequest != null) {
                return ResponseEntity.ok().body(holidayRequest);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Holiday request with ID " + id + " not found");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday request: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveHolidayRequestByUserID/{userID}")
    public ResponseEntity<Object> getHolidayRequestsByUserID(@PathVariable int userID) {
        try {
            HolidaysRequest holidayRequest = holidayRequestDAO.getHolidayRequestsByUserID(userID);
            if (holidayRequest != null) {
                return ResponseEntity.ok().body(holidayRequest);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holiday requests found for User ID " + userID);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday requests: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveHolidayRequestByStatus/{status}")
    public ResponseEntity<Object> getHolidayRequestsByStatus(@PathVariable String status) {
        try {
            HolidaysRequest holidayRequest = holidayRequestDAO.getHolidayRequestsByStatus(status);
            if (holidayRequest != null) {
                return ResponseEntity.ok().body(holidayRequest);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holiday requests found with status '" + status + "'");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday requests: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveHolidayRequestsByDepartment/{departmentId}")
    public ResponseEntity<Object> getHolidayRequestsByDepartmentId(@PathVariable int departmentId) {
        try {
            List<HolidaysRequest> holidayRequests = holidayRequestDAO.getHolidayRequestsByDepartmentID(departmentId);
            if (!holidayRequests.isEmpty()) {
                return ResponseEntity.ok().body(holidayRequests);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holiday requests found for Department ID " + departmentId);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday requests: " + e.getMessage());
        }
    }

    @GetMapping("/RetrieveHolidayRequestsByDepartmentAndStatus/{departmentId}/{status}")
    public ResponseEntity<Object> getHolidayRequestsByDepartmentAndStatus(@PathVariable int departmentId, @PathVariable String status) {
        try {
            List<HolidaysRequest> holidayRequests = holidayRequestDAO.getHolidayRequestsByDepartmentAndStatus(departmentId, status);
            if (!holidayRequests.isEmpty()) {
                return ResponseEntity.ok().body(holidayRequests);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holiday requests found for Department ID " + departmentId + " with status '" + status + "'");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday requests: " + e.getMessage());
        }
    }


    @PostMapping("/CreateNewHolidayRequest")
    public ResponseEntity<Object> CreateHolidayRequest(@RequestBody HolidaysRequest holidaysRequest) {
        try {
            int result = holidayRequestDAO.createHolidayRequest(holidaysRequest);
            if (result > 0) {
                return new ResponseEntity<>("Holiday Request successfully created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to create Holiday Request ", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/DeleteHolidayRequest/{requestID}")
    public ResponseEntity<Object> deleteHolidayRequest(@PathVariable("requestID") int requestID) {
        try {
            int result = holidayRequestDAO.deleteHolidayRequests(requestID);
            if (result > 0) {
                return new ResponseEntity<>("Holiday Request successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete Holiday Request ", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}