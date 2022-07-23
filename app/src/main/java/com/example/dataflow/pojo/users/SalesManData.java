package com.example.dataflow.pojo.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SalesManData implements Serializable {
    @SerializedName("DealerISNBranch")
    private int DealerISNBranch;
    @SerializedName("Dealer_ISN")
    private int Dealer_ISN;
    @SerializedName("DealerName")
    private String DealerName;
    @SerializedName("BranchISN")
    private int BranchISN;

    public int getDealerISNBranch() {
        return DealerISNBranch;
    }

    public void setDealerISNBranch(int dealerISNBranch) {
        DealerISNBranch = dealerISNBranch;
    }

    public int getDealer_ISN() {
        return Dealer_ISN;
    }

    public void setDealer_ISN(int dealer_ISN) {
        Dealer_ISN = dealer_ISN;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }
}
