package com.example.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SeasonsList implements Serializable {
    @SerializedName("BranchISN")
    private int branchISN;
    @SerializedName("StoreSeason_ISN")
    private int storeSeasonISN;
    @SerializedName("StoreSeasonISNBranch")
    private int storeSeasonISNBranch;
    @SerializedName("StoreSeasonName")
    private String storeSeasonName;

    public int getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(int branchISN) {
        this.branchISN = branchISN;
    }

    public int getStoreSeasonISN() {
        return storeSeasonISN;
    }

    public void setStoreSeasonISN(int storeSeasonISN) {
        this.storeSeasonISN = storeSeasonISN;
    }

    public int getStoreSeasonISNBranch() {
        return storeSeasonISNBranch;
    }

    public void setStoreSeasonISNBranch(int storeSeasonISNBranch) {
        this.storeSeasonISNBranch = storeSeasonISNBranch;
    }

    public String getStoreSeasonName() {
        return storeSeasonName;
    }

    public void setStoreSeasonName(String storeSeasonName) {
        this.storeSeasonName = storeSeasonName;
    }
}
