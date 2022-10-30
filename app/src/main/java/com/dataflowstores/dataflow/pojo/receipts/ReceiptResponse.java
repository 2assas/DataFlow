package com.dataflowstores.dataflow.pojo.receipts;

import com.google.gson.annotations.SerializedName;

public class ReceiptResponse {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    ReceiptResponseData data=new ReceiptResponseData();
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ReceiptResponseData getData() {
        return data;
    }

    public void setData(ReceiptResponseData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
