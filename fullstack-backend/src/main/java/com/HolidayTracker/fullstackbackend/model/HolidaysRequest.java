package com.HolidayTracker.fullstackbackend.model;

public class HolidaysRequest {

    private long HolidayRequestID;
    private String Data;
    private long ID;
    private long ManagerID;
    private String RequestStatus;

    public long getHolidayRequestID() {
        return HolidayRequestID;
    }

    public void setHolidayRequestID(long holidayRequestID) {
        HolidayRequestID = holidayRequestID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getManagerID() {
        return ManagerID;
    }

    public void setManagerID(long managerID) {
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
