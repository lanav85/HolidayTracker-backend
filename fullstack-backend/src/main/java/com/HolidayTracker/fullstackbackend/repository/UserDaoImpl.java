package com.HolidayTracker.fullstackbackend.repository;
import com.HolidayTracker.fullstackbackend.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
     //CRUD - Retrieve
    // This method retrieves a user from the database based on the provided user ID.
     @Override
     public User get(int id) throws SQLException {
        Connection con = Database.getConnection(); // Establishes a database connection.
        User user = null;         // Initializes a User

        // SQL query to select user data based on user ID.
        String sql = "SELECT id, Email, Department, Data, ManagerID, UserType, HoursAllowance FROM users WHERE id = ?"; // "Where id = ?" only retrieve rows where the value in the id column matches the value that we'll specify later

        //PreparedStatement is an object with a parameterized query (SELECT * FROM users WHERE id = ?). The value of the parameter (userId) is set using setInt() method before executing the query.
        PreparedStatement ps = con.prepareStatement(sql);  // the sql is passed as an argument to the prepareStatement() method to create the PreparedStatement object ps.
        ps.setInt(1, id);  // Sets the user ID parameter in the SQL query.
        ResultSet rs = ps.executeQuery();  // ResultSet object holds the data from a database after executing a query. So this line will Execute the SQL query and retrieves the result set.


        // Checks if the result set has at least one row in the result set. If rs.next() returns true, indicating there is a row, the code inside the if block executes. However, it only executes once, regardless of the number of rows in the result set.
        if (rs.next()) {
            // Retrieves user data from the result set.
            int ID = rs.getInt("ID");
            String Email = rs.getString("Email");
            String Department = rs.getString("Department");
            String Data = rs.getString("Data");
            int ManagerId = rs.getInt("ManagerID");
            String UserType = rs.getString("UserType");
            int HoursAllowance = rs.getInt("HoursAllowance");

            // Creates a new User object with the retrieved data.
            user = new User(ID, Email, Department, Data, ManagerId, UserType, HoursAllowance);
        }
        // Closes the result set, prepared statement, and database connection to release resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        // Returns the retrieved User object, or null if no user was found.
        return user;
    }

    //CRUD -retrieve all
    @Override
    public List<User> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT id, Email, Department, Data, ManagerID,UserType, HoursAllowance FROM users ";

        List<User> users = new ArrayList<>();

        //Statement is used for executing a static SQL statement and returning the results it produces.
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int ID = rs.getInt("ID");
            String Email = rs.getString("Email");
            String Department = rs.getString("Department");
            String Data = rs.getString("Data");
            int ManagerId = rs.getInt("ManagerID");
            String UserType = rs.getString("UserType");
            int HoursAllowance = rs.getInt("HoursAllowance");

            User user = new User(ID, Email, Department, Data, ManagerId, UserType, HoursAllowance);
            users.add(user);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return users;
    }

    //CRUD - create or update
    @Override
    public void save(User user) throws SQLException {

    }

    // CRUD - Create
    @Override
    public int insert(User user) throws SQLException {
//1: connect to the database
        Connection con = Database.getConnection();
//2:define SQL
        String sql = "INSERT into users (Email, Department, Data, ManagerID, UserType, HoursAllowance) VALUES(?,?, ?::jsonb,?,?,?) ";
//3:Prepare statement
        PreparedStatement ps = con.prepareStatement(sql);
//4:  Sets the user parameters in the SQL query.
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getDepartment());
        ps.setString(3, user.getData());
        ps.setInt(4, user.getManagerID());
        ps.setString(5, user.getUserType());
        ps.setInt(6, user.getHoursAllowance());
        //5: Executes the SQL statement in this PreparedStatement object
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;

    }

    @Override
    public int update(User user) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE users set  Email = ?, Department = ?, Data = ?::jsonb, ManagerID = ?, UserType = ?, HoursAllowance = ? where id =? ";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, user.getEmail());
        ps.setString(2, user.getDepartment());
        ps.setString(3, user.getData());
        ps.setInt(4, user.getManagerID());
        ps.setString(5, user.getUserType());
        ps.setInt(6, user.getHoursAllowance());
        ps.setInt(7, user.getID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }

    @Override
    public int delete(User user) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE from users where id =? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, user.getID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }
}
