package com.example.dataflow.pojo.workStation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Branch implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private BranchData branchData;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BranchData getBranchData() {
        return branchData;
    }

    public void setBranchData(BranchData branchData) {
        this.branchData = branchData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
