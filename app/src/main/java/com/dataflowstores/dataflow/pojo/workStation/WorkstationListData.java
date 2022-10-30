package com.dataflowstores.dataflow.pojo.workStation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkstationListData implements Serializable {
    @SerializedName("Branch_ISN")
    long Branch_ISN;
    @SerializedName("BranchName")
    String BranchName;

    public long getBranch_ISN() {
        return Branch_ISN;
    }

    public void setBranch_ISN(long branch_ISN) {
        Branch_ISN = branch_ISN;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
}
