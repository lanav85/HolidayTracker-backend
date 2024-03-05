package com.HolidayTracker.fullstackbackend.repository.holidayRequests;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.repository.Dao;
import com.HolidayTracker.fullstackbackend.repository.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAOImpl implements Dao<HolidaysRequest> {

    public HolidaysRequest get(int id) throws SQLException {
        //1 - Connect to the database
        Connection con = Database.getConnection();
        // 2 - Initializes a holiday request
        HolidaysRequest holidayRequest = null;
        // 3 - SQL query to select holidayRequests data based on holiday request ID.
        String sql = "SELECT HolidayRequestID, Data, ID, ManagerID, RequestStatus FROM HolidaysRequests WHERE id = ?";
        //4 -Prepare Statement and Sets the holiday request ID parameter in the SQL query.
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        //5-  ResultSet object to hold the data from the database after executing a query.
        ResultSet rs = ps.executeQuery();
        //6- Retrieve Holiday Request data from the result set.
        if (rs.next()) {
            int HolidayRequestID = rs.getInt("HolidayRequestID");
            String Data = rs.getString("Data");
            int ID = rs.getInt("ID");
            int ManagerID = rs.getInt("ManagerID");
            String RequestStatus = rs.getString("RequestStatus");

         //7-  Create a new holidayrequest object with the retrieved data.
            holidayRequest = new HolidaysRequest(HolidayRequestID, Data, ID, ManagerID, RequestStatus);
        }
        //8- Close the result set, prepared statement, and database connection to release resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        //9-  Returns the retrieved holiday request object, or null if no holiday request was found.
        return holidayRequest;

    }

    @Override
    public List<HolidaysRequest> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT HolidayRequestID, Data, ID, ManagerID, RequestStatus FROM HolidaysRequests ";

        List<HolidaysRequest> holidaysRequests = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int HolidayRequestID = rs.getInt("HolidayRequestID");
            String Data = rs.getString("Data");
            int ID = rs.getInt("ID");
            int ManagerID = rs.getInt("ManagerID");
            String RequestStatus = rs.getString("RequestStatus");

            HolidaysRequest holidaysRequest = new HolidaysRequest(HolidayRequestID, Data, ID, ManagerID, RequestStatus);
            holidaysRequests.add(holidaysRequest);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return holidaysRequests;
    }

    @Override
    public int insert(HolidaysRequest holidaysRequest) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT into HolidaysRequests (Data, ID, ManagerID, RequestStatus) VALUES(?::jsonb,?,?,?) ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, holidaysRequest.getData());
        ps.setInt(2,holidaysRequest.getID());
        ps.setInt(3,holidaysRequest.getManagerID());
        ps.setString(4, holidaysRequest.getRequestStatus());
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;
    }

    @Override
    public int update(HolidaysRequest holidaysRequest) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE HolidaysRequests set  Data = ?::jsonb,ID = ?, ManagerID = ?, RequestStatus = ? where HolidayRequestID =? ";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, holidaysRequest.getData());
        ps.setInt(2,holidaysRequest.getID());
        ps.setInt(3,holidaysRequest.getManagerID());
        ps.setString(4, holidaysRequest.getRequestStatus());
        ps.setInt(5,holidaysRequest.getHolidayRequestID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }

    @Override
    public int delete(HolidaysRequest holidaysRequest) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE from HolidaysRequests where HolidayRequestID = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, holidaysRequest.getHolidayRequestID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;    }
}
