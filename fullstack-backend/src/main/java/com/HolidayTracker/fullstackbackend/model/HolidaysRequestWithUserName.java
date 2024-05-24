package com.HolidayTracker.fullstackbackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
@Scope("prototype")
@Component
public class HolidaysRequestWithUserName extends  HolidaysRequest{
    private String userName;
    private String roleName;
    private String departmentName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonCreator
    public HolidaysRequestWithUserName(int requestID, int userID, Date requestFrom, Date requestTo, String status, String userName, String roleName, String departmentName) {
        super(requestID, userID, requestFrom, requestTo, status);
        this.userName = userName;
        this.roleName =roleName;
        this.departmentName = departmentName;

    }

    public HolidaysRequestWithUserName(int userID, Date requestFrom, Date requestTo, String status, String userName, String roleName, String departmentName) {
        super(userID, requestFrom, requestTo, status);
        this.userName = userName;
        this.roleName =roleName;
        this.departmentName = departmentName;


    }
}
