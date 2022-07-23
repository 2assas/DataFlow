package com.example.dataflow.pojo.invoice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceResponse implements Serializable {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    InvoiceResponseData data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public InvoiceResponseData getData() {
        return data;
    }

    public void setData(InvoiceResponseData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
