package com.lakhpati.models;

public class UserCoinsModel {

    private int UserTransactionHistoryId;
    private int ConfigchoiceTransactionTypeId;
    private double Coins;
    private String FromUser;
    private String ToUser;
    private int UserDetailId;

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public int getUserTransactionHistoryId() {
        return UserTransactionHistoryId;
    }

    public void setUserTransactionHistoryId(int userTransactionHistoryId) {
        UserTransactionHistoryId = userTransactionHistoryId;
    }

    public int getConfigchoiceTransactionTypeId() {
        return ConfigchoiceTransactionTypeId;
    }

    public void setConfigchoiceTransactionTypeId(int configchoiceTransactionTypeId) {
        ConfigchoiceTransactionTypeId = configchoiceTransactionTypeId;
    }

    public double getCoins() {
        return Coins;
    }

    public void setCoins(double coins) {
        Coins = coins;
    }

    public String getFromUser() {
        return FromUser;
    }

    public void setFromUser(String fromUser) {
        FromUser = fromUser;
    }

    public String getToUser() {
        return ToUser;
    }

    public void setToUser(String toUser) {
        ToUser = toUser;
    }
}
