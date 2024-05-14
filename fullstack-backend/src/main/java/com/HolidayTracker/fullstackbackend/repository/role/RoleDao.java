package com.HolidayTracker.fullstackbackend.repository.role;

import com.HolidayTracker.fullstackbackend.model.Role;
import com.HolidayTracker.fullstackbackend.repository.Database;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDao {

    public Role get(int id) throws SQLException {
        Connection con = Database.getConnection();
        Role role = null;
        String sql = "SELECT RoleID, Level, RoleDescription, Approves FROM Role WHERE RoleID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int roleID = rs.getInt("RoleID");
            char level = rs.getString("Level").charAt(0);
            String roleDescription = rs.getString("RoleDescription");
            int approves = rs.getInt("Approves");
            role = new Role(roleID, level, roleDescription, approves);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return role;
    }

    public List<Role> getAll() throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RoleID, Level, RoleDescription, Approves FROM Role";
        List<Role> roles = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int roleID = rs.getInt("RoleID");
            char level = rs.getString("Level").charAt(0);
            String roleDescription = rs.getString("RoleDescription");
            int approves = rs.getInt("Approves");
            Role role = new Role(roleID, level, roleDescription, approves);
            roles.add(role);
        }
        Database.closeResultSet(rs);
        Database.closeStatement(stmt);
        Database.closeConnection(con);
        return roles;
    }

    public  List <Role> getRoleByRoleID(int roleID) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "SELECT RoleID, Level, RoleDescription, Approves FROM Role WHERE RoleID = ?";
        List<Role> roles = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, roleID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int roleId = rs.getInt("RoleID");
            char level = rs.getString("Level").charAt(0);
            String roleDescription = rs.getString("RoleDescription");
            int approves = rs.getInt("Approves");
            Role role = new Role(roleId, level, roleDescription, approves);
            roles.add(role);
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return roles;
    }

    public int insert(Role role) throws SQLException {
        Connection con = Database.getConnection();
        String sql = "INSERT INTO Role (RoleID, Level, RoleDescription, Approves) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, role.getRoleID());
        ps.setString(2, String.valueOf(role.getLevel()));
        ps.setString(3, role.getRoleDescription());
        ps.setInt(4, role.getApproves());
        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }

    public int update(Role role) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE Role SET RoleID = ?, Level = ?, RoleDescription = ?, Approves = ? WHERE RoleID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, role.getRoleID());
        ps.setString(2, String.valueOf(role.getLevel()));
        ps.setString(3, role.getRoleDescription());
        ps.setInt(4, role.getApproves());
        ps.setInt(5, role.getRoleID());
        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }

    public int delete(int id) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM Role WHERE RoleID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        int result = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return result;
    }
}
