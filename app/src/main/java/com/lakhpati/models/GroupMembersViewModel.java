package com.lakhpati.models;

public class GroupMembersViewModel {
    private String Name;
    private String EmailId;
    private boolean IsAdmin;
    private String InviteStatus;
    private int UserGroupId;
    private int GroupId;
    private int UserDetailId;
    private String DisplayName;


    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return EmailId;
    }

    public void setEmail(String emailId) {
        EmailId = emailId;
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

    public int getUserGroupId() {
        return UserGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        UserGroupId = userGroupId;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

}
