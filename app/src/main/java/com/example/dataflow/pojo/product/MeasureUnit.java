package com.example.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeasureUnit implements Serializable {
    @SerializedName("ItemBranchISN")
    private int itemBranchISN;
    @SerializedName("ItemISN")
    private int itemISN;
    @SerializedName("MeasureUnitBranchISN")
    private int measureUnitBranchISN;
    @SerializedName("MeasureUnitISN")
    private int measureUnitISN;
    @SerializedName("MeasureUnitArName")
    private String measureUnitArName;
    @SerializedName("BasicMeasureUnit")
    private double basicMeasureUnit;
    @SerializedName("BasicUnitQuantity")
    private double basicUnitQuantity;
    @SerializedName("DefaultMeasureUnit")
    private Boolean defaultMeasureUnit;
    @SerializedName("Price")
    private double price;
    @SerializedName("DealerSpecialPriceFound")
    private int SpecialDiscFound;

    public int getSpecialDiscFound() {
        return SpecialDiscFound;
    }

    public void setSpecialDiscFound(int specialDiscFound) {
        SpecialDiscFound = specialDiscFound;
    }

    public int getItemBranchISN() {
        return itemBranchISN;
    }

    public void setItemBranchISN(int itemBranchISN) {
        this.itemBranchISN = itemBranchISN;
    }

    public int getItemISN() {
        return itemISN;
    }

    public void setItemISN(int itemISN) {
        this.itemISN = itemISN;
    }

    public int getMeasureUnitBranchISN() {
        return measureUnitBranchISN;
    }

    public void setMeasureUnitBranchISN(int measureUnitBranchISN) {
        this.measureUnitBranchISN = measureUnitBranchISN;
    }

    public int getMeasureUnitISN() {
        return measureUnitISN;
    }

    public void setMeasureUnitISN(int measureUnitISN) {
        this.measureUnitISN = measureUnitISN;
    }

    public String getMeasureUnitArName() {
        return measureUnitArName;
    }

    public void setMeasureUnitArName(String measureUnitArName) {
        this.measureUnitArName = measureUnitArName;
    }

    public double getBasicMeasureUnit() {
        return basicMeasureUnit;
    }

    public void setBasicMeasureUnit(double basicMeasureUnit) {
        this.basicMeasureUnit = basicMeasureUnit;
    }

    public double getBasicUnitQuantity() {
        return basicUnitQuantity;
    }

    public void setBasicUnitQuantity(double basicUnitQuantity) {
        this.basicUnitQuantity = basicUnitQuantity;
    }

    public Boolean getDefaultMeasureUnit() {
        return defaultMeasureUnit;
    }

    public void setDefaultMeasureUnit(Boolean defaultMeasureUnit) {
        this.defaultMeasureUnit = defaultMeasureUnit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}