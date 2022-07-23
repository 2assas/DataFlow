package com.example.dataflow.pojo.report;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StoreReportModel implements Serializable {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    ArrayList<StoreReportData> data = new ArrayList<>();
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<StoreReportData> getData() {
        return data;
    }

    public void setData(ArrayList<StoreReportData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
