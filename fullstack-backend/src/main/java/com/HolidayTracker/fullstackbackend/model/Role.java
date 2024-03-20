package com.HolidayTracker.fullstackbackend.model;

public class Role {
    private int roleID;
    private char level;
    private String roleDescription;
    private int approves;

    public char getLevel() {
        return level;
    }

    public void setLevel(char level) {
        this.level = level;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public int getApproves() {
        return approves;
    }

    public void setApproves(int approves) {
        this.approves = approves;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleID=" + roleID +
                ", level=" + level +
                ", roleDescription='" + roleDescription + '\'' +
                ", approves=" + approves +
                '}';
    }
}
