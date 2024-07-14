package com.HolidayTracker.fullstackbackend.model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope ("prototype")
@Component

public class UserLogin {

    private String Email;
    private String Password;

    //Add constructor overload
    public UserLogin() {
    }
    public UserLogin(String email, String password){
        this.Email = email;
        this.Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}