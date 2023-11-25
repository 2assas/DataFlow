package com.dataflowstores.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Group2List implements Serializable {
    @SerializedName("BranchISN")
    private int branchISN;
    @SerializedName("StoreGroup2_ISN")
    private int storeGroup2ISN;
    @SerializedName("StoreGroup2ISNBranch")
    private int storeGroup2ISNBranch;
    @SerializedName("StoreGroup2Name")
    private String storeGroup2Name;

    public int getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(int branchISN) {
        this.branchISN = branchISN;
    }

    public int getStoreGroup2ISN() {
        return storeGroup2ISN;
    }

    public void setStoreGroup2ISN(int storeGroup2ISN) {
        this.storeGroup2ISN = storeGroup2ISN;
    }

    public int getStoreGroup2ISNBranch() {
        return storeGroup2ISNBranch;
    }

    public void setStoreGroup2ISNBranch(int storeGroup2ISNBranch) {
        this.storeGroup2ISNBranch = storeGroup2ISNBranch;
    }

    public String getStoreGroup2Name() {
        return storeGroup2Name;
    }

    public void setStoreGroup2Name(String storeGroup2Name) {
        this.storeGroup2Name = storeGroup2Name;
    }
}