package com.HolidayTracker.fullstackbackend.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Date;

@Scope("prototype")
@Component
public class DepartmentWithUserName extends Department{
    private String userName;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public DepartmentWithUserName(int departmentID, String departmentName, int userID, String userName) {
        super(departmentID, departmentName, userID);
        this.userName = userName;

    }
    @Override
    public String toString() {
        return "DepartmentWithUserName{" +
                "departmentID=" + getDepartmentID() +
                ", departmentName='" + getDepartmentName() + '\'' +
                ", userID=" + getUserID() +
                ", userName='" + userName + '\'' +
                '}';
    }

}
