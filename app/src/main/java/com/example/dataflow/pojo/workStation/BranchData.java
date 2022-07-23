package com.example.dataflow.pojo.workStation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BranchData implements Serializable {
    @SerializedName("Branch_ISN")
    private long branchISN;
    @SerializedName("BranchName")
    private String branchName;

    public long getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(long branchISN) {
        this.branchISN = branchISN;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
