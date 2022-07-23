package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoresData implements Serializable {
    @SerializedName("BranchISN")
    int BranchISN;
    @SerializedName("Store_ISN")
    int Store_ISN;
    @SerializedName("StoreName")
    String StoreName;

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }

    public int getStore_ISN() {
        return Store_ISN;
    }

    public void setStore_ISN(int store_ISN) {
        Store_ISN = store_ISN;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }
}
