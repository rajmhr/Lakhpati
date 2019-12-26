package com.lakhpati.models;

public class ReturnModel {
    private boolean success;
    private String returnData;
    private String message;

    public ReturnModel getGlobalErrorMessage(){
        setSuccess(false);
        setMessage("Cannot load data. Please check your connectivity.");
        return  this;
    }

    public ReturnModel getServerErrorMessage(){
        setSuccess(false);
        setMessage("Your server is currently under maintenance. Please try later.");
        return  this;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
