package com.HolidayTracker.fullstackbackend.model;

public class Department {

    private int departmentID;
    private String departmentName;
    private Integer userID;

    public Department(int departmentID, String departmentName, Integer userID) {
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

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
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
