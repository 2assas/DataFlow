package com.dataflowstores.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ColorsList implements Serializable {
    @SerializedName("BranchISN")
    private int branchISN;
    @SerializedName("StoreColor_ISN")
    private int storeColorISN;
    @SerializedName("StoreColorISNBranch")
    private int storeColorISNBranch;
    @SerializedName("StoreColorName")
    private String storeColorName;

    public int getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(int branchISN) {
        this.branchISN = branchISN;
    }

    public int getStoreColorISN() {
        return storeColorISN;
    }

    public void setStoreColorISN(int storeColorISN) {
        this.storeColorISN = storeColorISN;
    }

    public int getStoreColorISNBranch() {
        return storeColorISNBranch;
    }

    public void setStoreColorISNBranch(int storeColorISNBranch) {
        this.storeColorISNBranch = storeColorISNBranch;
    }

    public String getStoreColorName() {
        return storeColorName;
    }

    public void setStoreColorName(String storeColorName) {
        this.storeColorName = storeColorName;
    }
}