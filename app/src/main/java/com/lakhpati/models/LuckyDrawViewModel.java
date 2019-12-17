package com.lakhpati.models;

import java.util.Date;

public class LuckyDrawViewModel {
    private int GroupId;
    private int UserDetailId;
    private int GroupCampaignId;
    private String Winner;
    private String CampaignStatus;
    private Date CompletedDate;
    private String GroupName;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public int getGroupCampaignId() {
        return GroupCampaignId;
    }

    public void setGroupCampaignId(int groupCampaignId) {
        GroupCampaignId = groupCampaignId;
    }

    public String getWinner() {
        return Winner;
    }

    public void setWinner(String winner) {
        Winner = winner;
    }

    public String getCampaignStatus() {
        return CampaignStatus;
    }

    public void setCampaignStatus(String campaignStatus) {
        CampaignStatus = campaignStatus;
    }

    public Date getCompletedDate() {
        return CompletedDate;
    }

    public void setCompletedDate(Date completedDate) {
        CompletedDate = completedDate;
    }
}
