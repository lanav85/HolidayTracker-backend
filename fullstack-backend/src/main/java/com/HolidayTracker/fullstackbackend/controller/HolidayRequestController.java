package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.HolidayRequestDAO;
import com.HolidayTracker.fullstackbackend.service.CreateHolidayRequestValidator;
import com.HolidayTracker.fullstackbackend.service.UpdateHolidayRequestValidator;
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
    private CreateHolidayRequestValidator holidayRequestValidator;
    private UpdateHolidayRequestValidator updateHolidayRequestValidator;

    public HolidayRequestController(CreateHolidayRequestValidator holidayRequestValidator, HolidayRequestDAO holidayRequestDAO, UpdateHolidayRequestValidator updateHolidayRequestValidator) {
        this.holidayRequestValidator = holidayRequestValidator;
        this.holidayRequestDAO = holidayRequestDAO;
        this.updateHolidayRequestValidator = updateHolidayRequestValidator;
    }


    @GetMapping("/holidayRequests")
    public ResponseEntity<Object> getHolidayRequests(@RequestParam(required = false) Integer userId,
                                                     @RequestParam(required = false) String status) {
        try {
            List<HolidaysRequest> holidayRequests;
            if (userId != null && status != null) {
                holidayRequests = holidayRequestDAO.getHolidayRequestsByUserIDAndStatus(userId, status);
            } else if (userId != null) {
                holidayRequests = holidayRequestDAO.getHolidayRequestsByUserID(userId);
            } else if (status != null) {
                holidayRequests = holidayRequestDAO.getHolidayRequestsByStatus(status);
            } else {
                holidayRequests = holidayRequestDAO.getAllHolidayRequests();
            }

            if (!holidayRequests.isEmpty()) {
                return ResponseEntity.ok().body(holidayRequests);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holiday requests found matching the criteria.");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving holiday requests: " + e.getMessage());
        }
    }

    @DeleteMapping("/HolidayRequest/{requestID}")
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

    @PostMapping("/HolidayRequest")
    public ResponseEntity<Object> CreateHolidayRequest(@RequestBody HolidaysRequest holidaysRequest) {
        try {
            // Validate the holiday request
            ResponseEntity<Object> validationResponse = holidayRequestValidator.validateHolidayRequest(holidaysRequest);
            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                return validationResponse;
            }
            try {
                int result = holidayRequestDAO.createHolidayRequest(holidaysRequest);
                if (result > 0) { //"result > 0" checks if database operation is successful
                    return new ResponseEntity<>("Holiday Request successfully created", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Unable to create Holiday Request ", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @PutMapping("/HolidayRequest/{requestID}")
    public ResponseEntity<Object> UpdateHolidayRequest(@PathVariable int requestID, @RequestBody HolidaysRequest holidaysRequest) {

        try {
            ResponseEntity<Object> validationResponse = updateHolidayRequestValidator.validateHolidayRequest(holidaysRequest);
            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                // Return validation error response
                return validationResponse;
            }
            try {
                int result = holidayRequestDAO.UpdateHolidayRequestStatus(holidaysRequest);
                if (result > 0) {
                    return new ResponseEntity<>("Holiday Request successfully Updated", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Unable to update Holiday Request ", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}