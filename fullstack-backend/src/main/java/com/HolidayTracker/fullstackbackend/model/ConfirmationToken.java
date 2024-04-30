package com.HolidayTracker.fullstackbackend.model;
import java.util.Date;

public class ConfirmationToken {

    private Long tokenId; // Unique identifier for the token
    private String confirmationToken; // The actual token string
    private Date createdDate; // Date when the token was created
    private User user; // The user associated with this token

    // Default constructor
    public ConfirmationToken() {
    }

    //  constructor to initialize all fields
    public ConfirmationToken(Long tokenId, String confirmationToken, Date createdDate, User user) {
        this.tokenId = tokenId;
        this.confirmationToken = confirmationToken;
        this.createdDate = createdDate;
        this.user = user;
    }


    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
