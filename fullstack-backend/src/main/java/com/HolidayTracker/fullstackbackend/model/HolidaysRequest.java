package com.HolidayTracker.fullstackbackend.model;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope ("prototype")
@Component

public class HolidaysRequest {

    private int requestID;
    private int userID;
    private Date requestFrom;
    private Date requestTo;
    private String status;

    public HolidaysRequest(){

    }

    public HolidaysRequest(int requestID, int userID, Date requestFrom, Date requestTo, String status) {
        this.requestID = requestID;
        this.userID = userID;
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.status = status;
    }

    public HolidaysRequest(int userID, Date requestFrom, Date requestTo, String status) {
   this(0,userID,requestFrom,requestTo,status);
    }
  /*  public HolidaysRequest(int userID, Date requestFrom, Date requestTo, String status) {
        this.userID = userID;
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.status = status;
    } */


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

    public java.sql.Date getRequestFrom() {
        return (java.sql.Date) requestFrom;
    }

    public void setRequestFrom(Date requestFrom) {
        this.requestFrom = requestFrom;
    }

    public java.sql.Date getRequestTo() {
        return (java.sql.Date) requestTo;
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
