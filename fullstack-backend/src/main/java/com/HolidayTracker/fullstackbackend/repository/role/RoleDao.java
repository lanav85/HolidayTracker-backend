package com.HolidayTracker.fullstackbackend.repository.role;
import com.HolidayTracker.fullstackbackend.model.Role;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDao {
    //CRUD - Retrieve role by RoleID
    // This method retrieves a role from the database based on the provided Role ID.
    public Role get(int id) throws SQLException {
        Connection con = Database.getConnection(); // Establishes a database connection.
        Role role = null;         // Initializes a Role

        // SQL query to select role data based on Role ID.

        String sql = "SELECT RoleID,Level,RoleDescription,Approves FROM Role WHERE ID = ?"; // "Where id = ?" only retrieve rows where the value in the id column matches the value that we'll specify later

        //PreparedStatement is an object with a parameterized query (SELECT * FROM role WHERE RoleID = ?). The value of the parameter (RoleId) is set using setInt() method before executing the query.
        PreparedStatement ps = con.prepareStatement(sql);  // the sql is passed as an argument to the prepareStatement() method to create the PreparedStatement object ps.
        ps.setInt(1, id);  // Sets the role ID parameter in the SQL query.
        ResultSet rs = ps.executeQuery();  // ResultSet object holds the data from a database after executing a query. So this line will Execute the SQL query and retrieves the result set.


        // Checks if the result set has at least one row in the result set. If rs.next() returns true, indicating there is a row, the code inside the if block executes. However, it only executes once, regardless of the number of rows in the result set.
        if (rs.next()) {
            // Retrieves role data from the result set.
            int RoleID = rs.getInt("RoleID");
            String Level = rs.getString("Level");
            String RoleDescription = rs.getString("RoleDescription");
            int Approves = rs.getInt("Approves");

            // Creates a new role object with the retrieved data.
            Role = new Role(RoleID,Level,RoleDescription,Approves);
        }
        // Closes the result set, prepared statement, and database connection to release resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        // Returns the retrieved Role object, or null if no role was found.
        return role;
    }
    //CRUD -retrieve all roles
    public List<Role> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RoleID,Level,RoleDescription,Approves FROM Role";

        List<Role> roles = new ArrayList<>();

        //Statement is used for executing a static SQL statement and returning the results it produces.
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int RoleID = rs.getInt("RoleID");
            String Level = rs.getString("Level");
            String RoleDescription = rs.getString("RoleDescription");
            int Approves = rs.getInt("Approves");

            Role role = new Role(RoleID,Level,RoleDescription,Approves);

            roles.add(role);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return roles;
    }
    //get list of roles by role ID
    public List<Role> getAllDRolesByRoleID(int roleID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RoleID,Level,RoleDescription,Approves FROM Role WHERE RoleID = ?";

        List<Roles> roles = new ArrayList<>();

        // Use PreparedStatement instead of Statement
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, roleID); // Set the RoleID parameter

        ResultSet rs = ps.executeQuery(); // Execute the query using PreparedStatement

        while (rs.next()) {
            int RoleID = rs.getInt("RoleID");
            String Level = rs.getString("Level");
            String RoleDescription = rs.getString("RoleDescription");
            int Approves = rs.getInt("Approves");

            Role role = new Role(RoleID,Level,RoleDescription,Approves);
            roles.add(role);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return roles;
    }
    // Create new role
    public int insert(Role role) throws SQLException {
//1: connect to the database
        Connection con = Database.getConnection();
//2:define SQL
        String sql = "INSERT into role (RoleID,Level,RoleDescription,Approves) VALUES(?,?,?,?) ";
//3:Prepare statement
        PreparedStatement ps = con.prepareStatement(sql);
//4:  Sets the role parameters in the SQL query.
        ps.setInt(1, user.getRoleID());
        ps.setString(2, user.getLevel());
        ps.setString(3, user.getRoleDescription());
        ps.setInt(4, user.getApproves());

        //5: Executes the SQL statement in this PreparedStatement object
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;

    }
    // Update role
    public int update(DRole role) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE role SET RoleID = ?, Level = ?, RoleDescriptioon = ?, Approves = ? WHERE RoleID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, user.getRoleID());
        ps.setString(2, user.getLevel());
        ps.setString(3, user.getRoleDescription());
        ps.setInt(4, user.getApproves());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }
    //Delete Role
    public int delete(int id) throws SQLException {

        Connection connection = Database.getConnection();
        String sql = "DELETE from role where RoleID =? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, id);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }
}
