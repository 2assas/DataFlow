package com.dataflowstores.dataflow.pojo.workStation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkstationList {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    List<WorkstationListData> data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<WorkstationListData> getData() {
        return data;
    }

    public void setData(List<WorkstationListData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
