package com.HolidayTracker.fullstackbackend.service;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Component
public class UpdateHolidayRequestValidator {
    private final UserDao userDao;
    private final ValidationLogic validationLogic;

    public UpdateHolidayRequestValidator(UserDao userDao, ValidationLogic validationLogic) {
        this.userDao = userDao;
        this.validationLogic = validationLogic;
    }

    @Transactional
    public ResponseEntity<Object> validateHolidayRequest(HolidaysRequest holidaysRequest) {
        try {
            // Fetch the existing holiday request from the database
            int requestId = holidaysRequest.getRequestID();
            HolidaysRequest existingRequest = getExistingHolidayRequest(requestId);

            // If the request is found and its status is "Pending" and the new status is "Accepted"
            if (existingRequest != null && existingRequest.getStatus().equalsIgnoreCase("Pending") && holidaysRequest.getStatus().equalsIgnoreCase("Accepted")) {
                // Deduct holiday entitlement from the user
                int userId = existingRequest.getUserID();
                User user = userDao.get(userId);
                int holidayEntitlement = user.getHolidayEntitlement();

                // Calculate total weekdays from holiday entitlement
                long totalWeekdays = validationLogic.countWeekdays(holidaysRequest.getRequestFrom(), holidaysRequest.getRequestTo());

                // Update user's holiday entitlement
                user.setHolidayEntitlement(holidayEntitlement - (int) totalWeekdays);
                userDao.update(user);

                // Return success response
                return ResponseEntity.ok("Holiday request validated successfully. Holiday entitlement deducted.");
            }
            // If the existing request status is "Accepted" and the new status is "Pending"
            else if (existingRequest != null && existingRequest.getStatus().equalsIgnoreCase("Accepted") && holidaysRequest.getStatus().equalsIgnoreCase("Rejected")) {
                // Add back the deducted days to the user's holiday entitlement
                int userId = existingRequest.getUserID();
                User user = userDao.get(userId);
                int holidayEntitlement = user.getHolidayEntitlement();

                // Calculate total weekdays from holiday entitlement
                long totalWeekdays = validationLogic.countWeekdays(holidaysRequest.getRequestFrom(), holidaysRequest.getRequestTo());

                // Update user's holiday entitlement
                user.setHolidayEntitlement(holidayEntitlement + (int) totalWeekdays);
                userDao.update(user);

                // Return success response
                return ResponseEntity.ok("Holiday request status reverted from Accepted to Pending. Deducted days added back to holiday entitlement.");
            }

            // Return response indicating no updates were made
            return ResponseEntity.ok("No updates required. Holiday request status remains unchanged.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUserHolidayEntitlement(int userId, HolidaysRequest holidaysRequest, boolean addBackDays) throws SQLException {
        // Retrieve information about the user related with the holiday request
        User user = userDao.get(userId);
        int holidayEntitlement = user.getHolidayEntitlement();
        // Calculate total weekdays from holiday entitlement
        long totalWeekdays = validationLogic.countWeekdays(holidaysRequest.getRequestFrom(), holidaysRequest.getRequestTo());

        // Add back or deduct the total weekdays from the user's holiday entitlement based on the 'addBackDays' flag
        if (addBackDays) {
            user.setHolidayEntitlement(holidayEntitlement + (int) totalWeekdays);
        } else {
            user.setHolidayEntitlement(holidayEntitlement - (int) totalWeekdays);
        }

        // Update user's holiday entitlement
        userDao.update(user);
    }

    private HolidaysRequest getExistingHolidayRequest(int requestId) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE RequestID = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, requestId);
        ResultSet rs = ps.executeQuery();

        HolidaysRequest existingRequest = null;
        if (rs.next()) {
            int userID = rs.getInt("UserID");
            Date requestFrom = rs.getDate("RequestFrom");
            Date requestTo = rs.getDate("RequestTo");
            String status = rs.getString("Status");

            existingRequest = new HolidaysRequest(requestId, userID, requestFrom, requestTo, status);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return existingRequest;
    }

}