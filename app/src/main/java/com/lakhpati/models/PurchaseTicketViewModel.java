package com.lakhpati.models;

import java.util.List;

public class PurchaseTicketViewModel {

    private int PurchaseTicketId;
    private int UserDetailId;
    private int LotteryGroupCampaignId;
    private List<String> TicketNos;

    public int getPurchaseTicketId() {
        return PurchaseTicketId;
    }

    public void setPurchaseTicketId(int purchaseTicketId) {
        PurchaseTicketId = purchaseTicketId;
    }

    public int getUserDetailId() {
        return UserDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        UserDetailId = userDetailId;
    }

    public int getLotteryGroupCampaignId() {
        return LotteryGroupCampaignId;
    }

    public void setLotteryGroupCampaignId(int lotteryGroupCampaignId) {
        LotteryGroupCampaignId = lotteryGroupCampaignId;
    }

    public List<String> getTicketNos() {
        return TicketNos;
    }

    public void setTicketNos(List<String> ticketNos) {
        TicketNos = ticketNos;
    }
}
