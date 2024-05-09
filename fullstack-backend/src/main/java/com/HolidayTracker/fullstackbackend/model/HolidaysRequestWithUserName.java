package com.HolidayTracker.fullstackbackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
@Scope("prototype")
@Component
public class HolidaysRequestWithUserName extends  HolidaysRequest{
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonCreator
    public HolidaysRequestWithUserName(int requestID, int userID, Date requestFrom, Date requestTo, String status, String userName) {
        super(requestID, userID, requestFrom, requestTo, status);
        this.userName = userName;

    }

    public HolidaysRequestWithUserName(int userID, Date requestFrom, Date requestTo, String status, String userName) {
        super(userID, requestFrom, requestTo, status);
        this.userName = userName;

    }
}
