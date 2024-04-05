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
    public User() {
    }
    public User(int userID, String data, String email, int holidayEntitlement, int departmentID, int roleID){
        this.UserID = userID;
        this.Data = data;
        this.Email = email;
        this.HolidayEntitlement = holidayEntitlement;
        this.DepartmentID = departmentID;
        this.RoleID = roleID;
    }
   // public User(String data, String email, int holidayEntitlement, int departmentID, int roleID) {
     //  this(0, data, email, holidayEntitlement, departmentID, roleID); // Set a default value for UserID
  // }

    public int getUserID() {
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