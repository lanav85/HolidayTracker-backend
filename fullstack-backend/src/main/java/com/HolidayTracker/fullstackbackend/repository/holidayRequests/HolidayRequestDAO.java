package com.HolidayTracker.fullstackbackend.repository.holidayRequests;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HolidayRequestDAO {
    // Get all holiday requests
    public List<HolidaysRequest> getAllHolidayRequests() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests";

        List<HolidaysRequest> holidayRequests = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int requestID = rs.getInt("RequestID");
                int userID = rs.getInt("UserID");
                Date requestFrom = rs.getDate("RequestFrom");
                Date requestTo = rs.getDate("RequestTo");
                String status = rs.getString("Status");

                HolidaysRequest holidayRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
                holidayRequests.add(holidayRequest);
            }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return holidayRequests;

    }
    // Get holiday request by ID
    public HolidaysRequest get(int id) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE RequestID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int requestID = rs.getInt("RequestID");
                    int userID = rs.getInt("UserID");
                    Date requestFrom = rs.getDate("RequestFrom");
                    Date requestTo = rs.getDate("RequestTo");
                    String status = rs.getString("Status");

                    return new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
                }
            }
        }
        Database.closeConnection(con);
        return null;
    }
    // Get holiday requests by userID
    public List<HolidaysRequest> getHolidayRequestsByUserID(int userID) throws SQLException {
        List<HolidaysRequest> holidayRequests = new ArrayList<>();
        Connection con = Database.getConnection();
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE UserID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int requestID = rs.getInt("RequestID");
                    Date requestFrom = rs.getDate("RequestFrom");
                    Date requestTo = rs.getDate("RequestTo");
                    String status = rs.getString("Status");

                    HolidaysRequest holidayRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
                    holidayRequests.add(holidayRequest);
                }
            }
        }
        Database.closeConnection(con);
        return holidayRequests;
    }
    // Get holiday requests by status
    public List<HolidaysRequest> getHolidayRequestsByStatus(String status) throws SQLException {
        List<HolidaysRequest> holidayRequests = new ArrayList<>();
        Connection con = Database.getConnection();
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE Status = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int requestID = rs.getInt("RequestID");
                    int userID = rs.getInt("UserID");
                    Date requestFrom = rs.getDate("RequestFrom");
                    Date requestTo = rs.getDate("RequestTo");

                    HolidaysRequest holidayRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
                    holidayRequests.add(holidayRequest);
                }
            }
        }
        Database.closeConnection(con);
        return holidayRequests;
    }
   //Get Holiday Request by department
   public List<HolidaysRequest> getHolidayRequestsByDepartment(String departmentName) throws SQLException {
       List<HolidaysRequest> holidayRequests = new ArrayList<>();
       Connection con = Database.getConnection();
       String sql = "SELECT Requests.RequestID, Requests.UserID, Requests.RequestFrom, Requests.RequestTo, Requests.Status" +
               "FROM Requests" +
               "JOIN Users ON Requests.UserID = Users.UserID" +
               "JOIN Department ON Users.DepartmentID = Department.DepartmentID" +
               "WHERE Department.DepartmentName = ?";
       try (PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setString(1, departmentName);
           try (ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {
                   int requestID = rs.getInt("RequestID");
                   int userID = rs.getInt("UserID");
                   Date requestFrom = rs.getDate("RequestFrom");
                   Date requestTo = rs.getDate("RequestTo");
                   String status = rs.getString("Status");

                   HolidaysRequest holidayRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
                   holidayRequests.add(holidayRequest);
               }
           }
       } finally {
           Database.closeConnection(con);
       }
       return holidayRequests;
   }
   //Get Holiday Request by department and status
   public List<HolidaysRequest> getHolidayRequestsByDepartmentAndStatus(String departmentName, String status) throws SQLException {
        List<HolidaysRequest> holidayRequests = new ArrayList<>();
        Connection con = Database.getConnection();
        String sql = "SELECT Requests.RequestID, Requests.UserID, Requests.RequestFrom, Requests.RequestTo, Requests.Status " +
                "FROM Requests  " +
                "JOIN Users  ON Requests.UserID = Users.UserID " +
                "JOIN Department ON Users.DepartmentID = Department.DepartmentID " +
                "WHERE Department.DepartmentName = ? AND Requests.Status = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, departmentName);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int requestID = rs.getInt("RequestID");
                    int userID = rs.getInt("UserID");
                    Date requestFrom = rs.getDate("RequestFrom");
                    Date requestTo = rs.getDate("RequestTo");
                    String statusFromDB = rs.getString("Status");

                    HolidaysRequest holidayRequest = new HolidaysRequest(requestID, userID, requestFrom, requestTo, statusFromDB);
                    holidayRequests.add(holidayRequest);
                }
            }
        } finally {
            Database.closeConnection(con);
        }
        return holidayRequests;
    }
    // Create new holiday request
    public int createHolidayRequest(HolidaysRequest request) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO Requests (UserID, RequestFrom, RequestTo, Status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, request.getUserID());
            ps.setDate(2, new java.sql.Date(request.getRequestFrom().getTime()));
            ps.setDate(3, new java.sql.Date(request.getRequestTo().getTime()));
            ps.setString(4, request.getStatus());
            ps.executeUpdate();
        }
        Database.closeConnection(con);
        return 0;
    }
    // Delete holiday request
    public void deleteHolidayRequest(int requestID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "DELETE FROM Requests WHERE RequestID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            ps.executeUpdate();
        }
        Database.closeConnection(con);
    }
}
