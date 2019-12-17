package com.lakhpati.models;

public class LotteryGroupModel {
    private String Name;
    private int GroupId;
    private int UserDetailId;
    private int UserGroupId;


    public int getUserGroupId() {
        return UserGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        UserGroupId = userGroupId;
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

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }


}
