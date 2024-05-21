package com.HolidayTracker.fullstackbackend.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class UserWithRoleName extends User {
    private String roleDescription;
    public UserWithRoleName(int userID, String data, String email, int holidayEntitlement, int departmentID, int roleID,  String roleDescription) {
        super(userID, data, email, holidayEntitlement, departmentID, roleID);
        this.roleDescription = roleDescription;
    }
    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
