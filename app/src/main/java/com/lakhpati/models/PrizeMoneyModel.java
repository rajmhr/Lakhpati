package com.lakhpati.models;

import java.util.Date;

public class PrizeMoneyModel {
    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getLotteryDefinition() {
        return LotteryDefination;
    }

    public void setLotteryDefinition(String lotteryDefinition) {
        LotteryDefination = lotteryDefinition;
    }

    public Date getCompletedDate() {
        return CompletedDate;
    }

    public void setCompletedDate(Date completedDate) {
        CompletedDate = completedDate;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
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

    public String getWinner() {
        return Winner;
    }

    public void setWinner(String winner) {
        Winner = winner;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public String getMyTickets() {
        return MyTickets;
    }

    public void setMyTickets(String myTickets) {
        MyTickets = myTickets;
    }

    public int getTotalTicketCount() {
        return TotalTicketCount;
    }

    public void setTotalTicketCount(int totalTicketCount) {
        TotalTicketCount = totalTicketCount;
    }

    public double getBetAmount() {
        return BetAmount;
    }

    public void setBetAmount(double betAmount) {
        BetAmount = betAmount;
    }

    public String getPaymentStatusDesc() {
        return PaymentStatusDesc;
    }

    public void setPaymentStatusDesc(String paymentStatusDesc) {
        PaymentStatusDesc = paymentStatusDesc;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public int getLotteryAdminGroupPaymentId() {
        return LotteryAdminGroupPaymentId;
    }

    public void setLotteryAdminGroupPaymentId(int lotteryAdminGroupPaymentId) {
        LotteryAdminGroupPaymentId = lotteryAdminGroupPaymentId;
    }

    private String GroupName;
    private String LotteryDefination;
    private Date CompletedDate;
    private Date StartDate;
    private String EmailId;
    private String UserDisplayName;
    private String Winner;
    private int UserDetailId;
    private String MyTickets;
    private int TotalTicketCount;
    private double BetAmount;
    private String PaymentStatusDesc;
    private String PaymentStatus;
    private String Remarks;
    private int LotteryAdminGroupPaymentId;
}
