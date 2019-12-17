package com.lakhpati.models.Pagination;

public class PaginationResponseModel {
    private String Data;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    private String Total;
}
