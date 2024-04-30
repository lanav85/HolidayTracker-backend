package com.HolidayTracker.fullstackbackend.repository.user;

import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.department.DepartmentDao;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {
    // CRUD - Retrieve user by userID
    // This method retrieves a user from the database by user ID.
    public User get(int id) throws SQLException {
        Connection con = Database.getConnection(); // Establishes a database connection.
        User user = null; // Initializes a User

        // SQL query to select user data based on user ID.

        String sql = "SELECT UserID,Data,Email,HolidayEntitlement,RoleID,DepartmentID FROM users WHERE UserID = ?"; // "Where
        // PreparedStatement is an object with a parameterized query (SELECT * FROM
        // users WHERE UserID = ?). The value of the parameter (userId) is set using
        // setInt() method before executing the query.
        PreparedStatement ps = con.prepareStatement(sql); // the sql is passed as an argument to the prepareStatement()
                                                          // method to create the PreparedStatement object ps.
        ps.setInt(1, id); // Sets the user ID parameter in the SQL query.
        ResultSet rs = ps.executeQuery(); // ResultSet object holds the data from a database after executing a query. So
                                          // this line will Execute the SQL query and retrieves the result set.

        // Checks if the result set has at least one row in the result set. If rs.next()
        // returns true, indicating there is a row, the code inside the if block
        // executes. However, it only executes once, regardless of the number of rows in
        // the result set.
        if (rs.next()) {
            // Retrieves user data from the result set.
            int UserID = rs.getInt("UserID");
            String Data = rs.getString("Data");
            String Email = rs.getString("Email");
            int HolidayEntitlement = rs.getInt("HolidayEntitlement");
            int RoleID = rs.getInt("RoleID");
            int DepartmentID = rs.getInt("DepartmentID");

            // Creates a new User object with the retrieved data.
            user = new User(UserID, Data, Email, HolidayEntitlement, RoleID, DepartmentID);

        }
        // Closes the result set, prepared statement, and database connection to release
        // resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        // Returns the retrieved User object, or null if no user was found.
        return user;
    }

    // Retrieve user by email
    public User getUserByEmail(String email) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;
        String sql = "SELECT UserID, Data, Email, HolidayEntitlement, RoleID, DepartmentID FROM users WHERE Email = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int UserID = rs.getInt("UserID");
            String Data = rs.getString("Data");
            String Email = rs.getString("Email");
            int HolidayEntitlement = rs.getInt("HolidayEntitlement");
            int RoleID = rs.getInt("RoleID");
            int DepartmentID = rs.getInt("DepartmentID");

            user = new User(UserID, Data, Email, HolidayEntitlement, RoleID, DepartmentID);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return user;
    }

    // CRUD -retrieve all users
    public List<User> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT UserID,Data,Email,HolidayEntitlement,DepartmentID,RoleID FROM users ";

        List<User> users = new ArrayList<>();

        // Statement is used for executing a static SQL statement and returning the
        // results it produces.
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int UserID = rs.getInt("UserID");
            String Data = rs.getString("Data");
            String Email = rs.getString("Email");
            int HolidayEntitlement = rs.getInt("HolidayEntitlement");
            int DepartmentID = rs.getInt("DepartmentID");
            int RoleID = rs.getInt("RoleID");

            User user = new User(UserID, Data, Email, HolidayEntitlement, DepartmentID, RoleID);

            users.add(user);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return users;
    }

    // get list of users by department ID
    public List<User> getAllUsersByDepartmentID(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT UserID,Data,Email,HolidayEntitlement,DepartmentID,RoleID FROM users WHERE DepartmentID = ?";

        List<User> users = new ArrayList<>();

        // Use PreparedStatement instead of Statement
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID); // Set the DepartmentID parameter

        ResultSet rs = ps.executeQuery(); // Execute the query using PreparedStatement

        while (rs.next()) {
            int UserID = rs.getInt("UserID");
            String Data = rs.getString("Data");
            String Email = rs.getString("Email");
            int HolidayEntitlement = rs.getInt("HolidayEntitlement");
            int DepartmentID = rs.getInt("DepartmentID");
            int RoleID = rs.getInt("RoleID");

            User user = new User(UserID, Data, Email, HolidayEntitlement, DepartmentID, RoleID);
            users.add(user);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return users;
    }

    // Create new user
    public int insert(User user) throws SQLException {
        // 1: connect to the database
        Connection con = Database.getConnection();
        // 2:define SQL
        String sql = "INSERT into users (Data,Email,HolidayEntitlement,DepartmentID,RoleID) VALUES(?::jsonb,?,?,?,?) ";
        // 3:Prepare statement
        PreparedStatement ps = con.prepareStatement(sql);
        // 4: Sets the user parameters in the SQL query.
        ps.setString(1, user.getData());
        ps.setString(2, user.getEmail());
        ps.setInt(3, user.getHolidayEntitlement());
        ps.setInt(4, user.getDepartmentID());
        ps.setInt(5, user.getRoleID());

        // 5: Executes the SQL statement in this PreparedStatement object
        int result = ps.executeUpdate();
        if (result > 0 && user.getRoleID() == 2) {
            // Update Department UserID if the user has roleID 2
            DepartmentDao departmentDAO = new DepartmentDao();
            departmentDAO.updateDepartmentUserID(user.getDepartmentID(), user.getUserID());
        }
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;

    }

    // Update user
    public int update(User user) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE users SET Data = ?::jsonb, Email = ?, HolidayEntitlement = ?, DepartmentID = ?, RoleID = ? WHERE UserID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, user.getData());
        ps.setString(2, user.getEmail());
        ps.setInt(3, user.getHolidayEntitlement());
        ps.setInt(4, user.getDepartmentID());
        ps.setInt(5, user.getRoleID());
        ps.setInt(6, user.getUserID());
        int result = ps.executeUpdate();
        if (result > 0 && user.getRoleID() == 2) {
            // Update Department UserID if the user has roleID 2
            DepartmentDao departmentDAO = new DepartmentDao();
            departmentDAO.updateDepartmentUserID(user.getDepartmentID(), user.getUserID());
        }

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }

    // Delete User
    public int delete(int id) throws SQLException {

        Connection connection = Database.getConnection();
        String sql = "DELETE from users where UserID =? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, id);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }

}
