package com.HolidayTracker.fullstackbackend.repository.department;

import com.HolidayTracker.fullstackbackend.model.Department;
import com.HolidayTracker.fullstackbackend.model.DepartmentWithUserName;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDao {

    public Department get(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        Department department = null;

        String sql = "SELECT DepartmentID, DepartmentName, UserID FROM Department WHERE DepartmentID = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int departmentIDResult = rs.getInt("DepartmentID");
            String departmentName = rs.getString("DepartmentName");
            int userID = rs.getInt("UserID");

            department = new Department(departmentIDResult, departmentName, userID);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return department;
    }

    public List<Department> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT Department.DepartmentID, Department.DepartmentName, Users.UserID, Users.Data->>'name' AS UserName " +
                "FROM Department " +
                "JOIN Users ON Department.UserID = Users.UserID ";

        List<Department> departments = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int departmentID = rs.getInt("DepartmentID");
            String departmentName = rs.getString("DepartmentName");
            int userID = rs.getInt("UserID");
            String userName = rs.getString("UserName");

            DepartmentWithUserName department = new DepartmentWithUserName(departmentID, departmentName, userID, userName);

            departments.add(department);
        }

        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);

        return departments;
    }

    public List<DepartmentWithUserName> getDepartmentsByDepartmentID(int departmentID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT Department.DepartmentID, Department.DepartmentName, Users.UserID, Users.Data->>'name' AS UserName " +
                "FROM Department " +
                "JOIN Users ON Department.UserID = Users.UserID " +
                "WHERE Department.DepartmentID = ?";

        List<DepartmentWithUserName> departments = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, departmentID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int departmentIDResult = rs.getInt("DepartmentID");
            String departmentName = rs.getString("DepartmentName");
            int userID = rs.getInt("UserID");
            String userName = rs.getString("UserName");

            DepartmentWithUserName department = new DepartmentWithUserName(departmentIDResult, departmentName, userID, userName);
            departments.add(department);
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return departments;
    }
    public int insert(Department department) throws SQLException {
        Connection con = Database.getConnection();

        String sql = "INSERT INTO Department (DepartmentID, DepartmentName, UserID) VALUES (?, ?, ?)";
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

        String sql = "UPDATE Department SET DepartmentName = ?, UserID = ? WHERE DepartmentID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, department.getDepartmentName());
        ps.setInt(2, department.getUserID());
        ps.setInt(3, department.getDepartmentID());

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }

    public int delete(int departmentID) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "DELETE FROM Department WHERE DepartmentID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, departmentID);

        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);

        return result;
    }
    // Method to update Department UserID (to update manager accordly)
    public void updateDepartmentUserID(int departmentID, int userID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "UPDATE Department SET UserID = ? WHERE DepartmentID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setInt(2, departmentID);
        ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
    }
}
