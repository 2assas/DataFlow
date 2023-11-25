package com.dataflowstores.dataflow.pojo.receipts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceiptModel implements Serializable {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    ArrayList<ReceiptData> data = new ArrayList<>();
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<ReceiptData> getData() {
        return data;
    }

    public void setData(ArrayList<ReceiptData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
