package com.lakhpati.models;

import java.util.Date;

public class MyTicketsViewModel {
    private String TicketNo;
    private Date PurchasedDate;

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public Date getPurchasedate() {
        return PurchasedDate;
    }

    public void setPurchasedate(Date purchasedate) {
        PurchasedDate = purchasedate;
    }
}
