package com.HolidayTracker.fullstackbackend.model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope ("prototype")
@Component

public class User {


    private long ID;
    private String Email;
    private String Department;
    private String Data;
    private int ManagerID;
    private String UserType;
    private int HoursAllowance;

    public User(int id, String email, String department, String data, int managerId, String userType, int hoursAllowance) {
        this(email, department, data, managerId, userType, hoursAllowance);
        this.ID = id;
    }

    public User(String email, String department, String data, int managerId, String userType, int hoursAllowance) {
        this.Email = email;
        this.Department = department;
        this.Data = data;
        this.ManagerID = managerId;
        this.UserType = userType;
        this.HoursAllowance = hoursAllowance;
    }


    public int getID() {

        return (int) ID;
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

    public int getHoursAllowance() {
        return HoursAllowance;
    }

    public void setHoursAllowance(int hoursAllowance) {
        HoursAllowance = hoursAllowance;
    }

    @Override
    public String toString() {
        return " TestDBConnection{" +
                "ID=" + ID +
                ", Email='" + Email + '\'' +
                ", Department='" + Department + '\'' +
                ", Data='" + Data + '\'' +
                ", ManagerID=" + ManagerID +
                ", UserType='" + UserType + '\'' +
                ", HoursAllowance=" + HoursAllowance +
                "}\n";
    }
}