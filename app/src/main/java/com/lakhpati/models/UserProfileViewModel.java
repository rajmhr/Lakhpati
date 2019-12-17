package com.lakhpati.models;

public class UserProfileViewModel {
    private String UserDisplayName ;
    private String EmailId ;
    private int NoOfGroups;

    public String getUserDisplayName() {
        return UserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        UserDisplayName = userDisplayName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public int getNoOfGroups() {
        return NoOfGroups;
    }

    public void setNoOfGroups(int noOfGroups) {
        NoOfGroups = noOfGroups;
    }

}
