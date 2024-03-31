package com.HolidayTracker.fullstackbackend.repository.department;
import com.HolidayTracker.fullstackbackend.model.Department;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDao {
    //CRUD - Retrieve department by departmentID
    // This method retrieves a department from the database based on the provided department ID.
    public Department get(int id) throws SQLException {
        Connection con = Database.getConnection(); // Establishes a database connection.
        Department department = null;         // Initializes a Department

        // SQL query to select department data based on Department ID.

        String sql = "SELECT DepartmentID,DptName,UserID FROM Departments WHERE ID = ?"; // "Where id = ?" only retrieve rows where the value in the id column matches the value that we'll specify later

        //PreparedStatement is an object with a parameterized query (SELECT * FROM departmentss WHERE DepartmentID = ?). The value of the parameter (departmentId) is set using setInt() method before executing the query.
        PreparedStatement ps = con.prepareStatement(sql);  // the sql is passed as an argument to the prepareStatement() method to create the PreparedStatement object ps.
        ps.setInt(1, id);  // Sets the department ID parameter in the SQL query.
        ResultSet rs = ps.executeQuery();  // ResultSet object holds the data from a database after executing a query. So this line will Execute the SQL query and retrieves the result set.


        // Checks if the result set has at least one row in the result set. If rs.next() returns true, indicating there is a row, the code inside the if block executes. However, it only executes once, regardless of the number of rows in the result set.
        if (rs.next()) {
            // Retrieves department data from the result set.
            int DptID = rs.getInt("DptID");
            String DptName = rs.getString("DptName");
            int UserID = rs.getInt("UserID");

            // Creates a new Department object with the retrieved data.
            Department = new Department(DepartmentID,DptName,UserID);
        }
        // Closes the result set, prepared statement, and database connection to release resources.
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        // Returns the retrieved Department object, or null if no department was found.
        return department;
    }
    //CRUD -retrieve all departmentss
    public List<Department> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT DepartmentID,DptName,UserID FROM Departments";

        List<Department> departments = new ArrayList<>();

        //Statement is used for executing a static SQL statement and returning the results it produces.
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int DptID = rs.getInt("DptID");
            String DptName = rs.getString("DptName");
            int UserID = rs.getInt("UserID");

            Department department = new Department(DepartmentID,DptName,UserID);

            departments.add(department);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return departments;
    }
    //get list of departments by department ID
    public List<Department> getAllDepartmentsByDepartmentID(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT DepartmentID,DptName,UserID FROM Departments WHERE DepartmentID = ?";

        List<Departments> departments = new ArrayList<>();

        // Use PreparedStatement instead of Statement
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID); // Set the DepartmentID parameter

        ResultSet rs = ps.executeQuery(); // Execute the query using PreparedStatement

        while (rs.next()) {
            int DptID = rs.getInt("DptID");
            String DptName = rs.getString("DptName");
            int UserID = rs.getInt("UserID");

            Department department = new Department(DepartmentID,DptName,UserID);
            departments.add(department);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return departments;
    }
    // Create new department
    public int insert(Department department) throws SQLException {
//1: connect to the database
        Connection con = Database.getConnection();
//2:define SQL
        String sql = "INSERT into departments (DepartmentID,DptName,UserID) VALUES(?,?,?) ";
//3:Prepare statement
        PreparedStatement ps = con.prepareStatement(sql);
//4:  Sets the department parameters in the SQL query.
        ps.setInt(1, user.getDptID());
        ps.setString(2, user.getDptName());
        ps.setInt(3, user.getUserID());

        //5: Executes the SQL statement in this PreparedStatement object
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;

    }
    // Update department
    public int update(Department department) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE departments SET DeptID = ?, DptName = ?, UserID = ? WHERE DptID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, user.getDptID());
        ps.setString(2, user.getDptName());
        ps.setInt(3, user.getUserID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }
    //Delete Department
    public int delete(int id) throws SQLException {

        Connection connection = Database.getConnection();
        String sql = "DELETE from departments where DptID =? ";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, id);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }
}
