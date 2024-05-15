package com.HolidayTracker.fullstackbackend.repository.holidayRequests;

import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequestWithUserName;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.service.CreateHolidayRequestValidator;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Repository
public class HolidayRequestDAO {
    public List<HolidaysRequest> getHolidayRequests(Integer requestId,
                                                    Integer userId,
                                                    Integer departmentId,
                                                    String status) throws SQLException {
        Connection con = Database.getConnection();
        String select_sql =
                "SELECT r.RequestID, r.UserID, r.RequestFrom, r.RequestTo, r.Status " +
                        "FROM Requests r " +
                        "JOIN Users u ON r.UserID = u.UserID " +
                        "JOIN Department d ON u.DepartmentID = d.DepartmentID";
        StringBuilder where_sql = new StringBuilder();
        if (requestId != null) {
            where_sql.append(where_sql.isEmpty() ? " WHERE" : " AND");
            where_sql.append(" r.RequestId = ?");
        }
        if (userId != null) {
            where_sql.append(where_sql.isEmpty() ? " WHERE" : " AND");
            where_sql.append(" r.UserID = ?");
        }
        if (departmentId != null) {
            where_sql.append(where_sql.isEmpty() ? " WHERE" : " AND");
            where_sql.append(" d.DepartmentID = ?");
        }
        if (status != null) {
            where_sql.append(where_sql.isEmpty() ? " WHERE" : " AND");
            where_sql.append(" r.Status = ?");
        }
        String sql = select_sql + where_sql;

        List<HolidaysRequest> holidayRequests = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(sql);
        int pi = 0;
        if (requestId != null) {
            pi++;
            ps.setInt(pi, requestId);
        }
        if (userId != null) {
            pi++;
            ps.setInt(pi, userId);
        }
        if (departmentId != null) {
            pi++;
            ps.setInt(pi, departmentId);
        }
        if (status != null) {
            pi++;
            ps.setString(pi, status);
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int resRequestID = rs.getInt("RequestID");
            int resUserID = rs.getInt("UserID");
            Date resRequestFrom = rs.getDate("RequestFrom");
            Date resRequestTo = rs.getDate("RequestTo");
            String resStatus = rs.getString("Status");
            HolidaysRequest holidayRequest = new HolidaysRequest(resRequestID, resUserID, resRequestFrom, resRequestTo, resStatus);
            holidayRequests.add(holidayRequest);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return holidayRequests;
    }

    // Delete holiday request
    public int deleteHolidayRequests(int id) throws SQLException {

        Connection connection = Database.getConnection();
        String sql = "DELETE from Requests where RequestID  = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, id);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }

    //Update Holiday Request Status
    public int UpdateHolidayRequestStatus(HolidaysRequest holidaysRequest) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE Requests SET Status = ? WHERE RequestID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        // Set parameters for the update query
        ps.setString(1, holidaysRequest.getStatus());
        ps.setInt(2, holidaysRequest.getRequestID());

        int result = ps.executeUpdate();

        // Close resources
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }

    // Create new holiday request
    public int createHolidayRequest(HolidaysRequest request) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO Requests (UserID, RequestFrom, RequestTo, Status) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, request.getUserID());
        ps.setDate(2, new java.sql.Date(request.getRequestFrom().getTime()));
        ps.setDate(3, new java.sql.Date(request.getRequestTo().getTime()));
        ps.setString(4, request.getStatus());

        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }


}