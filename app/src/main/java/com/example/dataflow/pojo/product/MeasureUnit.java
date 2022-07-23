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
    private int basicMeasureUnit;
    @SerializedName("BasicUnitQuantity")
    private int basicUnitQuantity;
    @SerializedName("DefaultMeasureUnit")
    private Boolean defaultMeasureUnit;
    @SerializedName("Price")
    private double price;

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

    public int getBasicMeasureUnit() {
        return basicMeasureUnit;
    }

    public void setBasicMeasureUnit(int basicMeasureUnit) {
        this.basicMeasureUnit = basicMeasureUnit;
    }

    public int getBasicUnitQuantity() {
        return basicUnitQuantity;
    }

    public void setBasicUnitQuantity(int basicUnitQuantity) {
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