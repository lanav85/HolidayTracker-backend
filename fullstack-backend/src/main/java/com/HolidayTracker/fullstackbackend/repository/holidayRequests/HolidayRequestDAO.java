package com.HolidayTracker.fullstackbackend.repository.holidayRequests;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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
    public HolidaysRequest getHolidayRequestByID(int id) throws SQLException {
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
    // Get holiday requests by userID
    public HolidaysRequest getHolidayRequestsByUserID(int userID) throws SQLException {
        Connection con = Database.getConnection();
        HolidaysRequest holidayRequests = null;

        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE UserID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int requestID = rs.getInt("RequestID");
            Date requestFrom = rs.getDate("RequestFrom");
            Date requestTo = rs.getDate("RequestTo");
            String status = rs.getString("Status");

            holidayRequests = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return holidayRequests;
    }
    // Get holiday requests by status
    public HolidaysRequest getHolidayRequestsByStatus(String status) throws SQLException {
        Connection con = Database.getConnection();
        HolidaysRequest holidayRequests = null;

        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo, Status FROM Requests WHERE Status = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int requestID = rs.getInt("RequestID");
            int userID = rs.getInt("UserID");
            Date requestFrom = rs.getDate("RequestFrom");
            Date requestTo = rs.getDate("RequestTo");

            holidayRequests = new HolidaysRequest(requestID, userID, requestFrom, requestTo, status);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return holidayRequests;
    }
    //Get Holiday Request by department
    public List<HolidaysRequest> getHolidayRequestsByDepartmentID(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String sql =  "SELECT Requests.RequestID, Requests.UserID, Requests.RequestFrom, Requests.RequestTo, Requests.Status " +
                "FROM Requests " +
                "JOIN Users ON Requests.UserID = Users.UserID " +
                "JOIN Department ON Users.DepartmentID = Department.DepartmentID " +
                "WHERE Department.DepartmentID = ?";
        List<HolidaysRequest> holidayRequests = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID);

        ResultSet rs = ps.executeQuery();

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
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return holidayRequests;

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
}
