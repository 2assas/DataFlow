package com.example.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Group1List implements Serializable {
    @SerializedName("BranchISN")
    private int branchISN;
    @SerializedName("StoreGroup1_ISN")
    private int storeGroup1ISN;
    @SerializedName("StoreGroup1ISNBranch")
    private int storeGroup1ISNBranch;
    @SerializedName("StoreGroup1Name")
    private String storeGroup1Name;

    public int getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(int branchISN) {
        this.branchISN = branchISN;
    }

    public int getStoreGroup1ISN() {
        return storeGroup1ISN;
    }

    public void setStoreGroup1ISN(int storeGroup1ISN) {
        this.storeGroup1ISN = storeGroup1ISN;
    }

    public int getStoreGroup1ISNBranch() {
        return storeGroup1ISNBranch;
    }

    public void setStoreGroup1ISNBranch(int storeGroup1ISNBranch) {
        this.storeGroup1ISNBranch = storeGroup1ISNBranch;
    }

    public String getStoreGroup1Name() {
        return storeGroup1Name;
    }

    public void setStoreGroup1Name(String storeGroup1Name) {
        this.storeGroup1Name = storeGroup1Name;
    }
}