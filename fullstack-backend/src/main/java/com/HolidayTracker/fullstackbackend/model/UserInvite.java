package com.HolidayTracker.fullstackbackend.model;

public class UserInvite {
    private long InviteID;
    private String Email;
    private long ManagerID;
    private String RequestStatus;
    public long getInviteID() {
        return InviteID;
    }

    public void setInviteID(long inviteID) {
        InviteID = inviteID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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
        return "UserInvite{" +
                "InviteID=" + InviteID +
                ", Email='" + Email + '\'' +
                ", ManagerID=" + ManagerID +
                ", RequestStatus='" + RequestStatus + '\'' +
                '}';
    }
}
