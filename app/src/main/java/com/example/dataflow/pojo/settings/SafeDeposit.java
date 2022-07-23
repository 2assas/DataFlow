package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SafeDeposit implements Serializable {

    @SerializedName("status")
    int status;
    @SerializedName("data")
    List<SafeDepositData> data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SafeDepositData> getData() {
        return data;
    }

    public void setData(List<SafeDepositData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
