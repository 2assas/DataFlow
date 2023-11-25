package com.dataflowstores.dataflow.pojo.receipts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiptResponseData implements Serializable {
    @SerializedName("Move_ID")
    long moveId;


    public long getMoveId() {
        return moveId;
    }

    public void setMoveId(long moveId) {
        this.moveId = moveId;
    }
}
