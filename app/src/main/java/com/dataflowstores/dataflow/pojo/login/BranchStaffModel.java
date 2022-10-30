package com.dataflowstores.dataflow.pojo.login;

import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;

public class BranchStaffModel {
    Branches branchDataList;
    Stores storeDataList;
    SafeDeposit safeDepositDataList;

    public BranchStaffModel(Branches branchDataList, Stores storeDataList, SafeDeposit safeDepositDataList) {
        this.branchDataList = branchDataList;
        this.storeDataList = storeDataList;
        this.safeDepositDataList = safeDepositDataList;
    }

    public Branches getBranchDataList() {
        return branchDataList;
    }

    public Stores getStoreDataList() {
        return storeDataList;
    }

    public SafeDeposit getSafeDepositDataList() {
        return safeDepositDataList;
    }
}
