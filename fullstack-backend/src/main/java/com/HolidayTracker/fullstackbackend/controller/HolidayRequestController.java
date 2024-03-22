package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.HolidayRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class HolidayRequestController {
    @Autowired
    private HolidayRequestDAO holidayRequestDAO;


    @GetMapping("/RetrieveHolidayRequestbyID/{userId}")
    public HolidaysRequest getHolidayRequestbyID(@PathVariable("userId") int userId) throws SQLException {
        return holidayRequestDAO.get(userId);
    }


    @GetMapping("/RetrieveAllholidayRequestS")
    public List<HolidaysRequest> getAll() throws SQLException {
        return holidayRequestDAO.getAll();
    }

    @PostMapping("/CreateNewHolidayRequest")
    public ResponseEntity<Object> createUser(@RequestBody HolidaysRequest holidaysRequest) {
        try {
            int result = holidayRequestDAO.insert(holidaysRequest);
            if (result > 0) {
                return new ResponseEntity<>("Holiday Request successfully created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to create Holiday Request ", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/DeleteHolidayRequest/{RequestID}")
    public ResponseEntity<Object> deleteHolidayRequestById(@PathVariable int RequestID) throws SQLException {

        int holidaysRequest = RequestID;

        try {
            int result = RequestID;
            if (result > 0) {
                return new ResponseEntity<>("Holiday Request  successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to delete Holiday Request ", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}