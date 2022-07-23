package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Stores implements Serializable {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    List<StoresData> data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<StoresData> getData() {
        return data;
    }

    public void setData(List<StoresData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
