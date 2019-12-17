package com.lakhpati.models;

public class AllUserTicketsViewModel {

    private String TicketNo;
    private String PurchasedDate;
    private String Email;
    private String DisplayName;

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public String getPurchasedDate() {
        return PurchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        PurchasedDate = purchasedDate;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}
