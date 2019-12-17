package com.lakhpati.models;

public class NotificationModel {

    private int notificationId;
    private int groupId;
    private String message;
    private int targetUserDetailId;
    private int fromUserDetailId;
    private String messageType;
    private String fromUserName;
    private String targetUserName;
    public String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTargetUserDetailId() {
        return targetUserDetailId;
    }

    public void setTargetUserDetailId(int targetUserDetailId) {
        this.targetUserDetailId = targetUserDetailId;
    }

    public int getFromUserDetailId() {
        return fromUserDetailId;
    }

    public void setFromUserDetailId(int fromUserDetailId) {
        this.fromUserDetailId = fromUserDetailId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }
}
