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


    public CreateHolidayRequestValidator(HolidayRequestDAO holidayRequestDAO, UserDao userDao) {
        this.holidayRequestDAO = holidayRequestDAO;
        this.userDao = userDao;
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
            long totalWorkingDays = countWeekdays(requestFrom, requestTo);
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

    public long countWeekdays(Date requestFrom, Date requestTo) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(requestFrom);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(requestTo);

        long weekdays = 0;
        // Iterate through each day between the start and end dates
        while (!startCalendar.after(endCalendar)) {
            int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                weekdays++;
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return weekdays;
    }

    public HolidaysRequest getHolidayRequestDates(int id) throws SQLException {
        Connection con = Database.getConnection(); // Establishes a database connection.
        HolidaysRequest holidaysRequest = null;

        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE RequestID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int requestID = rs.getInt("RequestID");
            int userID = rs.getInt("UserID");
            Date requestFrom = rs.getDate("RequestFrom");
            Date requestTo = rs.getDate("RequestTo");
            String status = rs.getString("Status");

            holidaysRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
        }
        // Closes the result set, prepared statement, and database connection to release resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        // Returns the retrieved User object, or null if no user was found.
        return holidaysRequest;
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
