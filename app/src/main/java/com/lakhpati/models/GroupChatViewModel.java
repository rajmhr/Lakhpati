package com.lakhpati.models;

public class GroupChatViewModel {
    private int chatGroupMessageId;
    private int fromUserDetailId;
    private int groupId;
    private String message;
    private String userDisplayName;
    private String fromEmailId;
    private String receivedTime;
    private String groupName;

    public int getChatGroupMessageId() {
        return chatGroupMessageId;
    }

    public void setChatGroupMessageId(int chatGroupMessageId) {
        this.chatGroupMessageId = chatGroupMessageId;
    }

    public int getFromUserDetailId() {
        return fromUserDetailId;
    }

    public void setFromUserDetailId(int fromUserDetailId) {
        this.fromUserDetailId = fromUserDetailId;
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

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getFromEmailId() {
        return fromEmailId;
    }

    public void setFromEmailId(String fromEmailId) {
        this.fromEmailId = fromEmailId;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
