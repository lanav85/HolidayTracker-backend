package com.HolidayTracker.fullstackbackend.repository;
import com.HolidayTracker.fullstackbackend.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

//CRUD - Retrieve
public class UserDaoImpl implements UserDao {
    @Override

    public User get(int id) throws SQLException {
        Connection con = Database.getConnection();
        User user = null;

        String sql = "SELECT id, Email, Department, Data, ManagerID,UserType, HoursAllowance FROM users WHERE id = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int ID = rs.getInt("ID");
            String Email = rs.getString("Email");
            String Department = rs.getString("Department");
            String Data = rs.getString("Data");
            int ManagerId = rs.getInt("ManagerID");
            String UserType = rs.getString("UserType");
            int HoursAllowance = rs.getInt("HoursAllowance");

            user = new User(ID, Email, Department, Data, ManagerId, UserType, HoursAllowance);
        }

        return user;
    }


    @Override
    public Optional<User> get(long id) throws SQLException {
        return Optional.empty();
    }

    //CRUD -retrieve all
    @Override
    public List<User> getAll() throws SQLException {
        return null;
    }

    //CRUD - create or update
    @Override
    public void save(User user) throws SQLException {

    }

    @Override
    public void insert(User user) throws SQLException {

    }

    @Override
    public void update(User user) throws SQLException {

    }

    @Override
    public void delete(User user) throws SQLException {

    }
}
