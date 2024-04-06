package com.HolidayTracker.fullstackbackend.repository.department;

import com.HolidayTracker.fullstackbackend.model.Department;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDao {

    public Department get(int id) throws SQLException {
        Connection con = Database.getConnection();
        Department department = null;

        String sql = "SELECT DepartmentID, DptName, UserID FROM Departments WHERE DepartmentID = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int departmentID = rs.getInt("DepartmentID");
            String departmentName = rs.getString("DptName");
            int userID = rs.getInt("UserID");

            department = new Department(departmentID, departmentName, userID);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return department;
    }

    public List<Department> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT DepartmentID, DptName, UserID FROM Departments";

        List<Department> departments = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int departmentID = rs.getInt("DepartmentID");
            String departmentName = rs.getString("DptName");
            int userID = rs.getInt("UserID");

            Department department = new Department(departmentID, departmentName, userID);

            departments.add(department);
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return departments;
    }

    public List<Department> getDepartmentsByDepartmentID(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT DepartmentID, DptName, UserID FROM Departments WHERE DepartmentID = ?";

        List<Department> departments = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int departmentIDResult = rs.getInt("DepartmentID");
            String departmentName = rs.getString("DptName");
            int userID = rs.getInt("UserID");

            Department department = new Department(departmentIDResult, departmentName, userID);
            departments.add(department);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return departments;
    }

    public int insert(Department department) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT INTO Departments (DepartmentID, DptName, UserID) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, department.getDepartmentID());
        ps.setString(2, department.getDepartmentName());
        ps.setInt(3, department.getUserID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return result;
    }

    public int update(Department department) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE Departments SET DptName = ?, UserID = ? WHERE DepartmentID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, department.getDepartmentName());
        ps.setInt(2, department.getUserID());
        ps.setInt(3, department.getDepartmentID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }

    public int delete(int id) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "DELETE FROM Departments WHERE DepartmentID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }
}
