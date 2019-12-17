package com.lakhpati.models;

import java.io.Serializable;
import java.util.Date;

public class RelatedLotteryGroupModel implements Serializable {
    private int GroupId;
    private String GroupName;
    private boolean IsAdmin;
    private String InviteStatus;
    private Date PeriodStart;
    private Date PeriodEnd;
    private String CampaignStatus;
    private String CampaignTitle;
    private int LotteryGroupCampaignId;
    private int LotteryUserGroupId;
    private int UserDetailId;
    private int BetCoin;
    private int MembersInGroup;

    public int getMembersInGroup() {
        return MembersInGroup;
    }

    public void setMembersInGroup(int membersInGroup) {
        MembersInGroup = membersInGroup;
    }

    public int getBetCoin() {
        return BetCoin;
    }

    public void setBetCoin(int betCoin) {
        BetCoin = betCoin;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public int getLotteryUserGroupId() {
        return LotteryUserGroupId;
    }

    public void setLotteryUserGroupId(int lotteryUserGroupId) {
        LotteryUserGroupId = lotteryUserGroupId;
    }

    public int getLotteryGroupCampaignId() {
        return LotteryGroupCampaignId;
    }

    public void setLotteryGroupCampaignId(int lotteryGroupCampaignId) {
        LotteryGroupCampaignId = lotteryGroupCampaignId;
    }

    public double getBetAmount() {
        return BetAmount;
    }

    public void setBetAmount(double betAmount) {
        BetAmount = betAmount;
    }

    private double BetAmount;

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

    public Date getPeriodStart() {
        return PeriodStart;
    }

    public void setPeriodStart(Date periodStart) {
        PeriodStart = periodStart;
    }

    public Date getPeriodEnd() {
        return PeriodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        PeriodEnd = periodEnd;
    }

    public String getCampaignStatus() {
        return CampaignStatus;
    }

    public void setCampaignStatus(String campaignStatus) {
        CampaignStatus = campaignStatus;
    }

    public String getCampaignTitle() {
        return CampaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        CampaignTitle = campaignTitle;
    }
}
