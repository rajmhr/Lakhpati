package com.lakhpati.models.Pagination;

public class PrizeMoneyPaginationRequestModel extends PaginationRequestModel {

    private int paymentStatusId;
    private int userDetailId;

    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public int getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        this.userDetailId = userDetailId;
    }
}
