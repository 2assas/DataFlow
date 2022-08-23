package com.example.dataflow.pojo.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private ArrayList<CustomerData> data;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<CustomerData> getData() {
        return data;
    }

    public void setData(ArrayList<CustomerData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
