package com.HolidayTracker.fullstackbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity  // An entity is a lightweight persistence domain object. Typically, an entity represents a table in a relational database
public class User {
@Id
@GeneratedValue
    private int ID;
    private String Email;
    private String Department;
    private String Data;
    private int ManagerID;
    private String UserType;
    private int HoursBalance;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public int getManagerID() {
        return ManagerID;
    }

    public void setManagerID(int managerID) {
        ManagerID = managerID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public int getHoursBalance() {
        return HoursBalance;
    }

    public void setHoursBalance(int hoursBalance) {
        HoursBalance = hoursBalance;
    }
}
