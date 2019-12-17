package com.lakhpati.models;

import java.util.Date;

public class LotteryGroupCampaignModel {

    private int GroupCampaignId;
    private int LotteryTypeId;
    private String Description;
    private int BetCoin;
    private Date PeriodStart;
    private Date PeriodEnd;
    private int GroupId;
    private int UserDetailId;
    private String GroupName;
    private int RoundNumber;

    public int getRoundNumber() {
        return RoundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        RoundNumber = roundNumber;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
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

    public void setGroupCampaignId(int campaignId) {
        GroupCampaignId = campaignId;
    }

    public int getLotteryTypeId() {
        return LotteryTypeId;
    }

    public void setLotteryTypeId(int lotteryTypeId) {
        LotteryTypeId = lotteryTypeId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getBetCoin() {
        return BetCoin;
    }

    public void setBetCoin(int betCoin) {
        BetCoin = betCoin;
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

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }
}
