package com.HolidayTracker.fullstackbackend.repository;
import com.HolidayTracker.fullstackbackend.model.User;

import java.sql.SQLException;

//The UserDao interface extends the Dao interface and specifies operations specifically
// related to the User entity. By extending the Dao interface, it inherits the generic CRUD methods
// but also adds additional methods that are specific to managing user data, such as get(int id).
public interface UserDao extends Dao<User>{


    User get(int id) throws SQLException;}

