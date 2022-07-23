package com.example.dataflow.pojo.workStation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Workstation implements Serializable {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    WorkstationData data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public WorkstationData getData() {
        return data;
    }

    public void setData(WorkstationData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
