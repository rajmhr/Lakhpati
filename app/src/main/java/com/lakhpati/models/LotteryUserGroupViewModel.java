package com.lakhpati.models;

public class LotteryUserGroupViewModel {
    private int GroupId;
    private String GroupName;
    private boolean IsAdmin;
    private String InviteStatus;
    private int LotteryUserGroupId;
    private String EmailId;
    private int UserDetailId;
    private String UserDisplayName;

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

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public String getInviteStatus() {
        return InviteStatus;
    }

    public void setInviteStatus(String inviteStatus) {
        InviteStatus = inviteStatus;
    }

    public int getLotteryUserGroupId() {
        return LotteryUserGroupId;
    }

    public void setLotteryUserGroupId(int lotteryUserGroupId) {
        LotteryUserGroupId = lotteryUserGroupId;
    }

}
