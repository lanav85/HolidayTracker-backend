package com.HolidayTracker.fullstackbackend.model;

public class HolidaysRequest {

    private int HolidayRequestID;
    private String Data;
    private int ID;
    private int ManagerID;
    private String RequestStatus;

    public HolidaysRequest( String data, int id, int managerID, String requestStatus) {
        this.Data = data;
        this.ID=id;
        this.ManagerID = managerID;
        this.RequestStatus = requestStatus;
    }
    public HolidaysRequest(int holidayRequestID, String data, int id, int managerID, String requestStatus) {
        this( data,  id,  managerID,  requestStatus);
        this.HolidayRequestID = holidayRequestID;

    }

    public int getHolidayRequestID() {
        return HolidayRequestID;
    }

    public void setHolidayRequestID(int holidayRequestID) {
        HolidayRequestID = holidayRequestID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getManagerID() {
        return ManagerID;
    }

    public void setManagerID(int managerID) {
        ManagerID = managerID;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }

    @Override
    public String toString() {
        return "HolidaysRequest{" +
                "HolidayRequestID=" + HolidayRequestID +
                ", Data='" + Data + '\'' +
                ", ID=" + ID +
                ", ManagerID=" + ManagerID +
                ", RequestStatus='" + RequestStatus + '\'' +
                '}';
    }
}
