package com.dataflowstores.dataflow.pojo.invoice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceResponseData implements Serializable {
    @SerializedName("BillNumber")
    int billNumber;
    @SerializedName("CurrentWorkingDayDate")
    String currentWorkingDayDate;
    @SerializedName("Move_ID")
    long move_ID;

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public String getCurrentWorkingDayDate() {
        return currentWorkingDayDate;
    }

    public void setCurrentWorkingDayDate(String currentWorkingDayDate) {
        this.currentWorkingDayDate = currentWorkingDayDate;
    }

    public long getMove_ID() {
        return move_ID;
    }

    public void setMove_ID(long move_ID) {
        this.move_ID = move_ID;
    }
}
