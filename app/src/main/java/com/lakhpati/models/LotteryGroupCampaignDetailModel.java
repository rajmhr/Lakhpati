package com.lakhpati.models;

import java.io.Serializable;
import java.util.Date;

public class LotteryGroupCampaignDetailModel implements Serializable {
    private int GroupId;
    private String GroupName;
    private boolean IsAdmin;
    private String InviteStatus;
    private Date PeriodEnd;
    private String CampaignStatus;
    private String CampaignTitle;
    private int GroupCampaignId;
    private int LotteryUserGroupId;
    private int UserDetailId;
    private int BetCoin;
    private int TotalTicketSold;
    private int BuyerUserCount;
    private int RoundNumber;

    public void setGroupCampaignId(int groupCampaignId) {
        GroupCampaignId = groupCampaignId;
    }

    public int getRoundNumber() {
        return RoundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        RoundNumber = roundNumber;
    }

    public int getTotalTicketSold() {
        return TotalTicketSold;
    }

    public void setTotalTicketSold(int totalTicketSold) {
        TotalTicketSold = totalTicketSold;
    }

    public int getBuyerUserCount() {
        return BuyerUserCount;
    }

    public void setBuyerUserCount(int buyerUserCount) {
        BuyerUserCount = buyerUserCount;
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

    public int getGroupCampaignId() {
        return GroupCampaignId;
    }

    public void setLotteryGroupCampaignId(int lotteryGroupCampaignId) {
        GroupCampaignId = lotteryGroupCampaignId;
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
