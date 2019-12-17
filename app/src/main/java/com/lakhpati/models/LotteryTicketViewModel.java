package com.lakhpati.models;

public class LotteryTicketViewModel {
    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    private String TicketNo;
    private int Count;

}
