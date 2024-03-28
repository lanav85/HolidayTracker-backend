package com.HolidayTracker.fullstackbackend.util;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.HolidayRequestDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class HolidayRequestValidator {
    private final HolidayRequestDAO holidayRequestDAO;

    public HolidayRequestValidator(HolidayRequestDAO holidayRequestDAO) {
        this.holidayRequestDAO = holidayRequestDAO;
    }

    public ResponseEntity<Object> validateHolidayRequest(HolidaysRequest holidaysRequest, User user) {
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
            if (hasOverlappingRequests(holidaysRequest)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Overlapping holiday requests detected.");
            }

            // check if Holiday Balance is valid
            long totalWorkingDays = countWeekdays(requestFrom, requestTo);
            int holidayEntitlement = user.getHolidayEntitlement();
            if (holidayEntitlement < totalWorkingDays) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient holiday balance.");
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

    public List<HolidaysRequest> getHolidayRequestDates(int UserID) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "SELECT RequestFrom, RequestTo, Status FROM Requests WHERE UserID = ?  AND status = 'pending' OR status = 'accepted'";
        List<HolidaysRequest> RequestDates = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, UserID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Date requestFrom = rs.getDate("RequestFrom");
            Date requestTo = rs.getDate("RequestTo");
            // Create a HolidaysRequest object and add it to the list
            HolidaysRequest request = new HolidaysRequest(requestFrom, requestTo);
            RequestDates.add(request);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return RequestDates;
    }
    public boolean hasOverlappingRequests (){


        return false;
    }
}
