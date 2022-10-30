package com.dataflowstores.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SafeDepositData implements Serializable {
    @SerializedName("BranchISN")
    int BranchISN;
    @SerializedName("SafeDeposit_ISN")
    int SafeDeposit_ISN;
    @SerializedName("SafeDepositName")
    String SafeDepositName;

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }

    public int getSafeDeposit_ISN() {
        return SafeDeposit_ISN;
    }

    public void setSafeDeposit_ISN(int safeDeposit_ISN) {
        SafeDeposit_ISN = safeDeposit_ISN;
    }

    public String getSafeDepositName() {
        return SafeDepositName;
    }

    public void setSafeDepositName(String safeDepositName) {
        SafeDepositName = safeDepositName;
    }
}
