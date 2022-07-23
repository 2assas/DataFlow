package com.example.dataflow.pojo.invoice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoveLines implements Serializable {
    @SerializedName("TotalQuantity")
    String TotalQuantity="";
    @SerializedName("Price")
    String Price="";
    @SerializedName("NetPrice")
    String NetPrice="";
    @SerializedName("LineNotes")
    String LineNotes="";
    @SerializedName("ItemSerial")
    String ItemSerial="";
    @SerializedName("ExpireDate")
    String ExpireDate="";
    @SerializedName("StoreColorName")
    String StoreColorName="";
    @SerializedName("StoreSizeName")
    String StoreSizeName="";
    @SerializedName("StoreSeasonName")
    String StoreSeasonName="";
    @SerializedName("StoreGroup2Name")
    String StoreGroup2Name="";
    @SerializedName("MeasureUnitISNBranch")
    String MeasureUnitISNBranch="";
    @SerializedName("MeasureUnitArName")
    String MeasureUnitArName="";
    @SerializedName("ItemBarCode")
    String ItemBarCode="";
    @SerializedName("PackageItemMain")
    String PackageItemMain="";
    @SerializedName("PackageItem")
    String PackageItem="";
    @SerializedName("ItemName")
    String ItemName="";

    @SerializedName("StoreNameFrom")
    String StoreNameFrom;

    @SerializedName("StoreNameTo")
    String StoreNameTo;

    public String getStoreNameFrom() {
        return StoreNameFrom;
    }

    public void setStoreNameFrom(String storeNameFrom) {
        StoreNameFrom = storeNameFrom;
    }

    public String getStoreNameTo() {
        return StoreNameTo;
    }

    public void setStoreNameTo(String storeNameTo) {
        StoreNameTo = storeNameTo;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNetPrice() {
        return NetPrice;
    }

    public void setNetPrice(String netPrice) {
        NetPrice = netPrice;
    }

    public String getLineNotes() {
        return LineNotes;
    }

    public void setLineNotes(String lineNotes) {
        LineNotes = lineNotes;
    }

    public String getItemSerial() {
        return ItemSerial;
    }

    public void setItemSerial(String itemSerial) {
        ItemSerial = itemSerial;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getStoreColorName() {
        return StoreColorName;
    }

    public void setStoreColorName(String storeColorName) {
        StoreColorName = storeColorName;
    }

    public String getStoreSizeName() {
        return StoreSizeName;
    }

    public void setStoreSizeName(String storeSizeName) {
        StoreSizeName = storeSizeName;
    }

    public String getStoreSeasonName() {
        return StoreSeasonName;
    }

    public void setStoreSeasonName(String storeSeasonName) {
        StoreSeasonName = storeSeasonName;
    }

    public String getStoreGroup2Name() {
        return StoreGroup2Name;
    }

    public void setStoreGroup2Name(String storeGroup2Name) {
        StoreGroup2Name = storeGroup2Name;
    }

    public String getMeasureUnitISNBranch() {
        return MeasureUnitISNBranch;
    }

    public void setMeasureUnitISNBranch(String measureUnitISNBranch) {
        MeasureUnitISNBranch = measureUnitISNBranch;
    }

    public String getMeasureUnitArName() {
        return MeasureUnitArName;
    }

    public void setMeasureUnitArName(String measureUnitArName) {
        MeasureUnitArName = measureUnitArName;
    }

    public String getItemBarCode() {
        return ItemBarCode;
    }

    public void setItemBarCode(String itemBarCode) {
        ItemBarCode = itemBarCode;
    }

    public String getPackageItemMain() {
        return PackageItemMain;
    }

    public void setPackageItemMain(String packageItemMain) {
        PackageItemMain = packageItemMain;
    }

    public String getPackageItem() {
        return PackageItem;
    }

    public void setPackageItem(String packageItem) {
        PackageItem = packageItem;
    }
}
