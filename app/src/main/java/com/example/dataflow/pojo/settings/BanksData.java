package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BanksData implements Serializable {
    @SerializedName("BranchISN")
    int BranchISN;
    @SerializedName("Bank_ISN")
    int Bank_ISN;
    @SerializedName("BankISNBranch")
    String BankISNBranch;
    @SerializedName("BankName")
    String BankName;

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }

    public int getBank_ISN() {
        return Bank_ISN;
    }

    public void setBank_ISN(int bank_ISN) {
        Bank_ISN = bank_ISN;
    }

    public String getBankISNBranch() {
        return BankISNBranch;
    }

    public void setBankISNBranch(String bankISNBranch) {
        BankISNBranch = bankISNBranch;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }
}
