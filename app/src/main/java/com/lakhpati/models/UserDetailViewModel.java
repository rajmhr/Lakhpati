package com.lakhpati.models;

import java.io.Serializable;
import java.util.Date;

public class UserDetailViewModel implements Serializable {

    private String DisplayName;
    private String EmailId;
    private String UserStatus;
    private String IsActive;
    private Date CreatedDate;
    private double Coins;

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public double getCoins() {
        return Coins;
    }

    public void setCoins(double coins) {
        Coins = coins;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    private int UserDetailId;

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        this.DisplayName = displayName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String email) {
        this.EmailId = email;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        this.IsActive = isActive;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.CreatedDate = createdDate;
    }


}
