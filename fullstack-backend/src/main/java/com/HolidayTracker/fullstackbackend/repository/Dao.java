package com.HolidayTracker.fullstackbackend.repository;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    //DAO API https://www.baeldung.com/java-dao-pattern
//DAO = Data Access Object class

   // Optional<T> get(long id) throws SQLException;

    List<T> getAll() throws SQLException;
    void save(T t) throws SQLException;
    int insert(T t) throws SQLException;
    int update(T t) throws SQLException;
    int delete(T t) throws SQLException;



}
