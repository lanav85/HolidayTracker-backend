package com.HolidayTracker.fullstackbackend.repository.holidayRequests;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HolidayRequestDAO {

    public HolidaysRequest get(int id) throws SQLException {
        //1 - Connect to the database
        Connection con = Database.getConnection();
        // 2 - Initializes a holiday request
        HolidaysRequest holidayRequest = null;
        // 3 - SQL query to select holidayRequests data based on holiday request ID.
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo,Status   FROM Requests WHERE RequestID = ?";
        //4 -Prepare Statement and Sets the holiday request ID parameter in the SQL query.
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,id);
        //5-  ResultSet object to hold the data from the database after executing a query.
        ResultSet rs = ps.executeQuery();
        //6- Retrieve Holiday Request data from the result set.
        if (rs.next()) {
            int RequestID = rs.getInt("RequestID");
            int UserID = rs.getInt("UserID");
            Date RequestFrom = rs.getDate("RequestFrom");
            Date RequestTo = rs.getDate("RequestTo");
            String Status = rs.getString("Status");

            //7-  Create a new holidayrequest object with the retrieved data.
            holidayRequest = new HolidaysRequest(RequestID, UserID, RequestFrom, RequestTo,Status);
        }
        //8- Close the result set, prepared statement, and database connection to release resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        //9-  Returns the retrieved holiday request object, or null if no holiday request was found.
        return holidayRequest;

    }
    public HolidaysRequest getRequestbyUserID(int id) throws SQLException {
        Connection con = Database.getConnection();
        HolidaysRequest holidayRequest = null;
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo,Status FROM Requests WHERE UserID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int RequestID = rs.getInt("RequestID");
            int UserID = rs.getInt("UserID");
            Date RequestFrom = rs.getDate("RequestFrom");
            Date RequestTo = rs.getDate("RequestTo");
            String Status = rs.getString("Status");

            holidayRequest = new HolidaysRequest(RequestID, UserID, RequestFrom, RequestTo,Status);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return holidayRequest;

    }
    public HolidaysRequest getRequestByStatus (int id) throws SQLException {
        Connection con = Database.getConnection();
        HolidaysRequest holidayRequest = null;
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo,Status FROM Requests WHERE Status = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int RequestID = rs.getInt("RequestID");
            int UserID = rs.getInt("UserID");
            Date RequestFrom = rs.getDate("RequestFrom");
            Date RequestTo = rs.getDate("RequestTo");
            String Status = rs.getString("Status");

            holidayRequest = new HolidaysRequest(RequestID, UserID, RequestFrom, RequestTo,Status);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return holidayRequest;

    }

    public List<HolidaysRequest> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RequestID, UserID, RequestFrom, RequestTo,Status FROM Requests ";

        List<HolidaysRequest> holidaysRequests = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int RequestID = rs.getInt("RequestID");
            int UserID = rs.getInt("UserID");
            Date RequestFrom = rs.getDate("RequestFrom");
            Date RequestTo = rs.getDate("RequestTo");
            String Status = rs.getString("Status");


            HolidaysRequest holidaysRequest = new HolidaysRequest(RequestID, UserID, RequestFrom, RequestTo,Status);
            holidaysRequests.add(holidaysRequest);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return holidaysRequests;
    }

    public int insert(HolidaysRequest holidaysRequest) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT into Requests (UserID, RequestFrom, RequestTo,Status) VALUES(?,?,?,?) ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, holidaysRequest.getUserID());
        ps.setDate(2,holidaysRequest.getRequestFrom());
        ps.setDate(3,holidaysRequest.getRequestTo());
        ps.setString(4, holidaysRequest.getStatus());
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;
    }

    public int delete(int holidaysRequest) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE from Requests where RequestID = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, holidaysRequest);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;    }
}
