package com.HolidayTracker.fullstackbackend.constants;

public enum Roles {

    Manager(1),
    Supervisor(2),
    Employee(3);

    private int roleId;

    Roles(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId(){
        return this.roleId;
    }

}
