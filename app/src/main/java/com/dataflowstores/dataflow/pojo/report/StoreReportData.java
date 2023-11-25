package com.dataflowstores.dataflow.pojo.report;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreReportData implements Serializable {
    @SerializedName("ItemISNBranch")
    String ItemISNBranch;
    @SerializedName("ItemBranchISN")
    String ItemBranchISN;
    @SerializedName("ItemISN")
    String ItemISN;
    @SerializedName("ItemName")
    String ItemName;
    @SerializedName("TotalQuan")
    String TotalQuan;
    @SerializedName("MeasureUnitArName")
    String MeasureUnitArName;
    @SerializedName("StoreName")
    String StoreName;
    @SerializedName("quantity_description")
    String quantity_description;

    public String getItemISNBranch() {
        return ItemISNBranch;
    }

    public void setItemISNBranch(String itemISNBranch) {
        ItemISNBranch = itemISNBranch;
    }

    public String getItemBranchISN() {
        return ItemBranchISN;
    }

    public void setItemBranchISN(String itemBranchISN) {
        ItemBranchISN = itemBranchISN;
    }

    public String getItemISN() {
        return ItemISN;
    }

    public void setItemISN(String itemISN) {
        ItemISN = itemISN;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getTotalQuan() {
        return TotalQuan;
    }

    public void setTotalQuan(String totalQuan) {
        TotalQuan = totalQuan;
    }

    public String getMeasureUnitArName() {
        return MeasureUnitArName;
    }

    public void setMeasureUnitArName(String measureUnitArName) {
        MeasureUnitArName = measureUnitArName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getQuantity_description() {
        return quantity_description;
    }

    public void setQuantity_description(String quantity_description) {
        this.quantity_description = quantity_description;
    }
}
