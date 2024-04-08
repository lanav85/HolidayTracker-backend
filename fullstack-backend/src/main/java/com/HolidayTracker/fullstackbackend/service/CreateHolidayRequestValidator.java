package com.HolidayTracker.fullstackbackend.service;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.HolidayRequestDAO;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class CreateHolidayRequestValidator {
    private final HolidayRequestDAO holidayRequestDAO;
    private final UserDao userDao;
    private final ValidationLogic validationLogic;


    public CreateHolidayRequestValidator(HolidayRequestDAO holidayRequestDAO, UserDao userDao, ValidationLogic validationLogic) {
        this.holidayRequestDAO = holidayRequestDAO;
        this.userDao = userDao;
        this.validationLogic = validationLogic;
    }

    public ResponseEntity<Object> validateHolidayRequest(HolidaysRequest holidaysRequest) {
        try {
            Date requestFrom = holidaysRequest.getRequestFrom();
            Date requestTo = holidaysRequest.getRequestTo();

            // A holiday request has to be done at least one day in advance
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();

            if (requestFrom.before(tomorrow)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Holiday request must be made at least one day in advance.");
            }

            // Retrieve user from the database
            int userId = holidaysRequest.getUserID();
            User user = userDao.get(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
            }
            // Check if Holiday Balance is enough
            long totalWorkingDays = validationLogic.countWeekdays(requestFrom, requestTo);
            int holidayEntitlement = user.getHolidayEntitlement();
            if (holidayEntitlement < totalWorkingDays) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient holiday balance.");
            }

            // Check for overlapping holiday requests
            if (hasOverlappingRequests(requestFrom, requestTo, userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Overlapping holiday requests.");
            }

            // If all validations pass, return a success response
            return ResponseEntity.ok("Holiday request validated successfully.");

        } catch (Exception e) {
            // Handle any exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating holiday request: " + e.getMessage());
        }
    }

    public List <HolidaysRequest> getHolidayRequestDates(int userID) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE UserID = ? AND (status = 'Pending' OR status = 'Accepted')";
        List<HolidaysRequest> holidayRequests = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int requestID = rs.getInt("RequestID");
            Date requestFrom = rs.getDate("RequestFrom");
            Date requestTo = rs.getDate("RequestTo");
            String status = rs.getString("Status");

            HolidaysRequest holidayRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
            holidayRequests.add(holidayRequest);

        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return holidayRequests;
    }

    public boolean hasOverlappingRequests(Date newRequestFrom, Date newRequestTo, int userID) throws SQLException {
        List<HolidaysRequest> previousHolidayRequests = (List<HolidaysRequest>) getHolidayRequestDates(userID);
        boolean overlapFound = false;

        for (HolidaysRequest previousRequest : previousHolidayRequests) {
            Date existingRequestFrom = previousRequest.getRequestFrom();
            Date existingRequestTo = previousRequest.getRequestTo();

            // Check if either existingRequestFrom or existingRequestTo is null
            if (existingRequestFrom == null || existingRequestTo == null) {
                // Handle the case where existingRequestFrom or existingRequestTo is null
                // You may want to log a warning or handle this case according to your application logic
                continue;
            }

            // Check for overlap
            if (newRequestFrom.before(existingRequestTo) && newRequestTo.after(existingRequestFrom)) {
                overlapFound = true;
                break;
            }
        }
        return overlapFound;
    }


}
