package com.lakhpati.models;

import java.util.Date;

public class GroupChatModel {

    private int GroupId;
    private String Message;
    private Date CreatedDate;
    private String UserDisplayName;
    private int UserDetailId;
    private String EmailId;
    private int FromUserDetailId;

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public String getUserDisplayName() {
        return UserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        UserDisplayName = userDisplayName;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public int getFromUserDetailId() {
        return FromUserDetailId;
    }

    public void setFromUserDetailId(int fromUserDetailId) {
        FromUserDetailId = fromUserDetailId;
    }
}
