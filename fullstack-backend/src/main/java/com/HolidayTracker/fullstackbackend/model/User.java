package com.HolidayTracker.fullstackbackend.model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope ("prototype")
@Component

public class User {

    private int UserID;
    private String Data;
    private String Email;
    private int HolidayEntitlement;
    private int DepartmentID;
    private int RoleID;

    //Add constructor overload


    public User(int userID, String data, String email, int holidayEntitlement, int departmentID, int roleID) {
        UserID = userID;
        Data = data;
        Email = email;
        HolidayEntitlement = holidayEntitlement;
        DepartmentID = departmentID;
        RoleID = roleID;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getHolidayEntitlement() {
        return HolidayEntitlement;
    }

    public void setHolidayEntitlement(int holidayEntitlement) {
        HolidayEntitlement = holidayEntitlement;
    }

    public int getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(int departmentID) {
        DepartmentID = departmentID;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserID=" + UserID +
                ", Data='" + Data + '\'' +
                ", Email='" + Email + '\'' +
                ", HolidayEntitlement=" + HolidayEntitlement +
                ", DepartmentID=" + DepartmentID +
                ", RoleID=" + RoleID +
                '}';
    }
}