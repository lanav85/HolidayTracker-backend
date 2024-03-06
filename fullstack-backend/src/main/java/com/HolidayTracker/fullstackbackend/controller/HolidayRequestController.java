package com.HolidayTracker.fullstackbackend.controller;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.RequestDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class HolidayRequestController {
    @Autowired
    private RequestDAOImpl requestDAO;

    @GetMapping("/retrieveHolidayRequestbyID")
    public HolidaysRequest getHolidayRequestById(@PathVariable int id) throws SQLException {
        return requestDAO.get(id);
    }

    @GetMapping("/retrieveHolidayRequestbyUserID")
    public HolidaysRequest getHolidayRequestByUserId(@PathVariable int id) throws SQLException {
        return requestDAO.getbyUserID(id);
    }

    @GetMapping("/retrieveHolidayRequestbyManagerID")
    public HolidaysRequest getHolidayRequestByManagerId(@PathVariable int id) throws SQLException {
        return requestDAO.getbyManagerID(id);
    }

    @GetMapping("/retrieveAllHolidayRequest")
    public List<HolidaysRequest> getAllHolidayRequests() throws SQLException {
        return requestDAO.getAll();

    }

    @PostMapping("/createHolidayRequest")
    public int createHolidayRequest (@RequestBody HolidaysRequest holidaysRequest) throws SQLException {
        return requestDAO.insert(holidaysRequest);
    }

    @DeleteMapping ("/DeleteHolidayRequest")
    public int deleteHolidayRequest (@PathVariable int id) throws SQLException{
        HolidaysRequest holidaysRequest = requestDAO.get(id);
        return requestDAO.delete(id);
    }
}
