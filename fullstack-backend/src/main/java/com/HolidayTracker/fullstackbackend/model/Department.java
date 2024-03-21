package com.HolidayTracker.fullstackbackend.model;

public class Department {

    private int departmentID;
    private String departmentName;
    private int userID;

    public Department(int departmentID, String departmentName, int userID) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.userID = userID;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentID=" + departmentID +
                ", departmentName='" + departmentName + '\'' +
                ", userID=" + userID +
                '}';
    }
}
