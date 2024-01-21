package com.dataflowstores.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchProductResponse implements Serializable {
    @SerializedName("ItemCode")
    String ItemCode="";

    @SerializedName("ItemName")
    String itemName;

    public String getItemCode() {
        return ItemCode;
    }
    public String getItemName() {
        return itemName;
    }
}
