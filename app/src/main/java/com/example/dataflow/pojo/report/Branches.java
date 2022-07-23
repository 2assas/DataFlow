package com.example.dataflow.pojo.report;

import com.example.dataflow.pojo.workStation.BranchData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Branches implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private List<BranchData> branchData;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<BranchData> getBranchData() {
        return branchData;
    }

    public void setBranchData(List<BranchData> branchData) {
        this.branchData = branchData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
