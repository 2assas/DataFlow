package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PriceTypeData implements Serializable {
    @SerializedName("BranchISN")
    int BranchISN;
    @SerializedName("PricesType_ISN")
    int PricesType_ISN;
    @SerializedName("PricesTypeISNBranch")
    int PricesTypeISNBranch;
    @SerializedName("PricesTypeName")
    String PricesTypeName;
    @SerializedName("Type")
    String Type;

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }

    public int getPricesType_ISN() {
        return PricesType_ISN;
    }

    public void setPricesType_ISN(int pricesType_ISN) {
        PricesType_ISN = pricesType_ISN;
    }

    public int getPricesTypeISNBranch() {
        return PricesTypeISNBranch;
    }

    public void setPricesTypeISNBranch(int pricesTypeISNBranch) {
        PricesTypeISNBranch = pricesTypeISNBranch;
    }

    public String getPricesTypeName() {
        return PricesTypeName;
    }

    public void setPricesTypeName(String pricesTypeName) {
        PricesTypeName = pricesTypeName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
