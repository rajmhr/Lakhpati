package com.lakhpati.models;

public class UserLotterySummaryModel {

    private int TotalGamePlayed;
    private double TotalCoinPlayed;
    private double TotalCoinWon;
    private double TotalCoinLost;
    private int TotalGameLost;
    private int TotalGameWon;
    private int UserDetailId;
    private String EmailId;
    private String DisplayName;
    private double Coins;

    public double getCoins() {
        return Coins;
    }

    public void setCoins(double coins) {
        Coins = coins;
    }

    public int getTotalGamePlayed() {
        return TotalGamePlayed;
    }

    public void setTotalGamePlayed(int totalGamePlayed) {
        TotalGamePlayed = totalGamePlayed;
    }

    public double getTotalCoinPlayed() {
        return TotalCoinPlayed;
    }

    public void setTotalCoinPlayed(double totalCoinPlayed) {
        TotalCoinPlayed = totalCoinPlayed;
    }

    public double getTotalCoinWon() {
        return TotalCoinWon;
    }

    public void setTotalCoinWon(double totalCoinWon) {
        TotalCoinWon = totalCoinWon;
    }

    public double getTotalCoinLost() {
        return TotalCoinLost;
    }

    public void setTotalCoinLost(double totalCoinLost) {
        TotalCoinLost = totalCoinLost;
    }

    public int getTotalGameLost() {
        return TotalGameLost;
    }

    public void setTotalGameLost(int totalGameLost) {
        TotalGameLost = totalGameLost;
    }

    public int getTotalGameWon() {
        return TotalGameWon;
    }

    public void setTotalGameWon(int totalGameWon) {
        TotalGameWon = totalGameWon;
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

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}
