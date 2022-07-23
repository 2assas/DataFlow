package com.example.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SizesList implements Serializable {
    @SerializedName("BranchISN")
    private int branchISN;
    @SerializedName("StoreSize_ISN")
    private int storeSizeISN;
    @SerializedName("StoreSizeISNBranch")
    private int storeSizeISNBranch;
    @SerializedName("StoreSizeName")
    private String storeSizeName;

    public int getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(int branchISN) {
        this.branchISN = branchISN;
    }

    public int getStoreSizeISN() {
        return storeSizeISN;
    }

    public void setStoreSizeISN(int storeSizeISN) {
        this.storeSizeISN = storeSizeISN;
    }

    public int getStoreSizeISNBranch() {
        return storeSizeISNBranch;
    }

    public void setStoreSizeISNBranch(int storeSizeISNBranch) {
        this.storeSizeISNBranch = storeSizeISNBranch;
    }

    public String getStoreSizeName() {
        return storeSizeName;
    }

    public void setStoreSizeName(String storeSizeName) {
        this.storeSizeName = storeSizeName;
    }
}
