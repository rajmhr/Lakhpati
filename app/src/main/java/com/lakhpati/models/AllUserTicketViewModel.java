package com.lakhpati.models;

import java.util.Date;

public class AllUserTicketViewModel {
    private String TicketNo;
    private Date PurchasedDate;
    private String EmailId;
    private String DisplayName;

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public Date getPurchasedDate() {
        return PurchasedDate;
    }

    public void setPurchasedDate(Date purchasedDate) {
        PurchasedDate = purchasedDate;
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
