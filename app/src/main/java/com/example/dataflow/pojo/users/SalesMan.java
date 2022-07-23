package com.example.dataflow.pojo.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SalesMan implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private ArrayList<SalesManData> data;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<SalesManData> getData() {
        return data;
    }

    public void setData(ArrayList<SalesManData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
