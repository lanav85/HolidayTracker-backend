package com.HolidayTracker.fullstackbackend.repository;
import com.HolidayTracker.fullstackbackend.model.User;

import java.sql.SQLException;

public interface UserDao extends Dao<User>{


    User get(int id) throws SQLException;
}

