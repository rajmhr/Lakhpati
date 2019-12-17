package com.lakhpati.models;

import java.util.Date;

public class NotificationSpModel {
    private int NotificationId;
    private int GroupId;
    private String Message;
    private int TargetUserDetailId;
    private int FromUserDetailId;
    private String MessageType;
    private String FromUserName;
    private String TargetUserName;
    private String GroupName;
    private Date CreatedDate;

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public int getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(int notificationId) {
        NotificationId = notificationId;
    }

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

    public int getTargetUserDetailId() {
        return TargetUserDetailId;
    }

    public void setTargetUserDetailId(int targetUserDetailId) {
        TargetUserDetailId = targetUserDetailId;
    }

    public int getFromUserDetailId() {
        return FromUserDetailId;
    }

    public void setFromUserDetailId(int fromUserDetailId) {
        FromUserDetailId = fromUserDetailId;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getTargetUserName() {
        return TargetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        TargetUserName = targetUserName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
}
