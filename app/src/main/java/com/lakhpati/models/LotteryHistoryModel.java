package com.lakhpati.models;

import java.util.Date;

public class LotteryHistoryModel {
    private String LotteryName;
    private String Winner;
    private String EmailId;
    private String UserDisplayName;
    private Date CompletedDate;
    private String MyTickets;
    private int TotalRows;
    private int CoinPrize;
    private int NoOfTicketSold;
    private int LotteryGroupCampaignId;

    public String getLotteryName() {
        return LotteryName;
    }

    public void setLotteryName(String lotteryName) {
        LotteryName = lotteryName;
    }

    public String getWinner() {
        return Winner;
    }

    public void setWinner(String winner) {
        Winner = winner;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getUserDisplayName() {
        return UserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        UserDisplayName = userDisplayName;
    }

    public Date getCompletedDate() {
        return CompletedDate;
    }

    public void setCompletedDate(Date completedDate) {
        CompletedDate = completedDate;
    }

    public String getMyTickets() {
        return MyTickets;
    }

    public void setMyTickets(String myTickets) {
        MyTickets = myTickets;
    }

    public int getTotalRows() {
        return TotalRows;
    }

    public void setTotalRows(int totalRows) {
        TotalRows = totalRows;
    }

    public int getCoinPrize() {
        return CoinPrize;
    }

    public void setCoinPrize(int prizeMoney) {
        CoinPrize = prizeMoney;
    }

    public int getNoOfTicketSold() {
        return NoOfTicketSold;
    }

    public void setNoOfTicketSold(int noOfTicketSold) {
        NoOfTicketSold = noOfTicketSold;
    }

    public int getLotteryGroupCampaignId() {
        return LotteryGroupCampaignId;
    }

    public void setLotteryGroupCampaignId(int groupCampaignId) {
        LotteryGroupCampaignId = groupCampaignId;
    }
}
