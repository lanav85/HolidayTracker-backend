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

    //constructor overload
    public User(int id, String email, int department, String data, int roleID,  int hoursAllowance) {
        this(email, department, data, roleID, hoursAllowance);
        this.UserID = id;
    }

    public User(String email, int department, String data, int roleID,  int hoursAllowance) {
        this.Email = email;
        this.DepartmentID = department;
        this.Data = data;
        this.RoleID = roleID;
        this.HolidayEntitlement = hoursAllowance;
    }
    public User(int id, int department, String data, int roleID,  int hoursAllowance) {
        this.UserID = id;
        this.DepartmentID = department;
        this.Data = data;
        this.RoleID = roleID;
        this.HolidayEntitlement = hoursAllowance;
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