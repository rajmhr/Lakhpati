package com.lakhpati.models;

import java.util.Date;

public class CoinHistoryModel {

    private int UserTransactionHistoryId;
    private int UserDetailId;
    private String TransactionType;
    private double Coins;
    private String FromUser;
    private String ToUser;
    private Date TransactionDate;
    private String TransactionTypeDesc;
    private double OutstandingCoin;

    public double getOutstandingCoin() {
        return OutstandingCoin;
    }

    public void setOutstandingCoin(double outstandingCoin) {
        OutstandingCoin = outstandingCoin;
    }

    public int getUserTransactionHistoryId() {
        return UserTransactionHistoryId;
    }

    public void setUserTransactionHistoryId(int userTransactionHistoryId) {
        UserTransactionHistoryId = userTransactionHistoryId;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
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

    public Date getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getTransactionTypeDesc() {
        return TransactionTypeDesc;
    }

    public void setTransactionTypeDesc(String transactionTypeDesc) {
        TransactionTypeDesc = transactionTypeDesc;
    }
}
