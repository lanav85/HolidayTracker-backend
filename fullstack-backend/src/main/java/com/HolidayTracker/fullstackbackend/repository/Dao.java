package com.HolidayTracker.fullstackbackend.repository;

import com.HolidayTracker.fullstackbackend.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    //DAO API https://www.baeldung.com/java-dao-pattern
//DAO = Data Access Object class

    Optional<T> get(long id) throws SQLException;

    List<T> getAll() throws SQLException;
    void save(T t) throws SQLException;
    void insert (T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;


}
