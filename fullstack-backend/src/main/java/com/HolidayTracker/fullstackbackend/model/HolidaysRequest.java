package com.HolidayTracker.fullstackbackend.model;

import java.util.Date;

public class HolidaysRequest {

    private int requestID;
    private int userID;
    private Date requestFrom;
    private Date requestTo;
    private String status;

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(Date requestFrom) {
        this.requestFrom = requestFrom;
    }

    public Date getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(Date requestTo) {
        this.requestTo = requestTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HolidaysRequest{" +
                "requestID=" + requestID +
                ", userID=" + userID +
                ", requestFrom=" + requestFrom +
                ", requestTo=" + requestTo +
                ", status='" + status + '\'' +
                '}';
    }
}
