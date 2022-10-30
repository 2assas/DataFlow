package com.dataflowstores.dataflow.pojo.report;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemAvailableQuantity implements Serializable {

	@SerializedName("StoreName")
	private String storeName;

	@SerializedName("ExpireDate")
	private String expireDate;

	@SerializedName("StoreSizeName")
	private String storeSizeName;

	@SerializedName("ItemISN")
	private String itemISN;

	@SerializedName("Group1BranchISN")
	private String group1BranchISN;

	@SerializedName("Group2BranchISN")
	private String group2BranchISN;

	@SerializedName("StoreColorName")
	private String storeColorName;

	@SerializedName("StoreGroup1Name")
	private String storeGroup1Name;

	@SerializedName("Group2ISN")
	private String group2ISN;

	@SerializedName("MeasureUnitArName")
	private String measureUnitArName;

	@SerializedName("SeasonISN")
	private String seasonISN;

	@SerializedName("SizeBranchISN")
	private String sizeBranchISN;

	@SerializedName("Group1ISN")
	private String group1ISN;

	@SerializedName("quantity_description")
	private String quantityDescription;

	@SerializedName("SeasonBranchISN")
	private String seasonBranchISN;

	@SerializedName("Serial")
	private String serial;

	@SerializedName("ItemBranchISN")
	private String itemBranchISN;

	@SerializedName("ColorBranchISN")
	private String colorBranchISN;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("SizeISN")
	private String sizeISN;

	@SerializedName("StoreSeasonName")
	private String storeSeasonName;

	@SerializedName("TotalQuan")
	private Double totalQuan;

	@SerializedName("ColorISN")
	private String colorISN;

	@SerializedName("StoreGroup2Name")
	private String storeGroup2Name;

	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

	public String getStoreName(){
		return storeName;
	}

	public void setExpireDate(String expireDate){
		this.expireDate = expireDate;
	}

	public String getExpireDate(){
		return expireDate;
	}

	public void setStoreSizeName(String storeSizeName){
		this.storeSizeName = storeSizeName;
	}

	public String getStoreSizeName(){
		return storeSizeName;
	}

	public void setItemISN(String itemISN){
		this.itemISN = itemISN;
	}

	public String getItemISN(){
		return itemISN;
	}

	public void setGroup1BranchISN(String group1BranchISN){
		this.group1BranchISN = group1BranchISN;
	}

	public String getGroup1BranchISN(){
		return group1BranchISN;
	}

	public void setGroup2BranchISN(String group2BranchISN){
		this.group2BranchISN = group2BranchISN;
	}

	public String getGroup2BranchISN(){
		return group2BranchISN;
	}

	public void setStoreColorName(String storeColorName){
		this.storeColorName = storeColorName;
	}

	public String getStoreColorName(){
		return storeColorName;
	}

	public void setStoreGroup1Name(String storeGroup1Name){
		this.storeGroup1Name = storeGroup1Name;
	}

	public String getStoreGroup1Name(){
		return storeGroup1Name;
	}

	public void setGroup2ISN(String group2ISN){
		this.group2ISN = group2ISN;
	}

	public String getGroup2ISN(){
		return group2ISN;
	}

	public void setMeasureUnitArName(String measureUnitArName){
		this.measureUnitArName = measureUnitArName;
	}

	public String getMeasureUnitArName(){
		return measureUnitArName;
	}

	public void setSeasonISN(String seasonISN){
		this.seasonISN = seasonISN;
	}

	public String getSeasonISN(){
		return seasonISN;
	}

	public void setSizeBranchISN(String sizeBranchISN){
		this.sizeBranchISN = sizeBranchISN;
	}

	public String getSizeBranchISN(){
		return sizeBranchISN;
	}

	public void setGroup1ISN(String group1ISN){
		this.group1ISN = group1ISN;
	}

	public String getGroup1ISN(){
		return group1ISN;
	}

	public void setQuantityDescription(String quantityDescription){
		this.quantityDescription = quantityDescription;
	}

	public String getQuantityDescription(){
		return quantityDescription;
	}

	public void setSeasonBranchISN(String seasonBranchISN){
		this.seasonBranchISN = seasonBranchISN;
	}

	public String getSeasonBranchISN(){
		return seasonBranchISN;
	}

	public void setSerial(String serial){
		this.serial = serial;
	}

	public String getSerial(){
		return serial;
	}

	public void setItemBranchISN(String itemBranchISN){
		this.itemBranchISN = itemBranchISN;
	}

	public String getItemBranchISN(){
		return itemBranchISN;
	}

	public void setColorBranchISN(String colorBranchISN){
		this.colorBranchISN = colorBranchISN;
	}

	public String getColorBranchISN(){
		return colorBranchISN;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setSizeISN(String sizeISN){
		this.sizeISN = sizeISN;
	}

	public String getSizeISN(){
		return sizeISN;
	}

	public void setStoreSeasonName(String storeSeasonName){
		this.storeSeasonName = storeSeasonName;
	}

	public String getStoreSeasonName(){
		return storeSeasonName;
	}

	public void setTotalQuan(Double totalQuan){
		this.totalQuan = totalQuan;
	}

	public Double getTotalQuan(){
		return totalQuan;
	}

	public void setColorISN(String colorISN){
		this.colorISN = colorISN;
	}

	public String getColorISN(){
		return colorISN;
	}

	public void setStoreGroup2Name(String storeGroup2Name){
		this.storeGroup2Name = storeGroup2Name;
	}

	public String getStoreGroup2Name(){
		return storeGroup2Name;
	}

	@Override
 	public String toString(){
		return 
			"ItemAvailableQuantity{" + 
			"storeName = '" + storeName + '\'' + 
			",expireDate = '" + expireDate + '\'' + 
			",storeSizeName = '" + storeSizeName + '\'' + 
			",itemISN = '" + itemISN + '\'' + 
			",group1BranchISN = '" + group1BranchISN + '\'' + 
			",group2BranchISN = '" + group2BranchISN + '\'' + 
			",storeColorName = '" + storeColorName + '\'' + 
			",storeGroup1Name = '" + storeGroup1Name + '\'' + 
			",group2ISN = '" + group2ISN + '\'' + 
			",measureUnitArName = '" + measureUnitArName + '\'' + 
			",seasonISN = '" + seasonISN + '\'' + 
			",sizeBranchISN = '" + sizeBranchISN + '\'' + 
			",group1ISN = '" + group1ISN + '\'' + 
			",quantity_description = '" + quantityDescription + '\'' + 
			",seasonBranchISN = '" + seasonBranchISN + '\'' + 
			",serial = '" + serial + '\'' + 
			",itemBranchISN = '" + itemBranchISN + '\'' + 
			",colorBranchISN = '" + colorBranchISN + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",sizeISN = '" + sizeISN + '\'' + 
			",storeSeasonName = '" + storeSeasonName + '\'' + 
			",totalQuan = '" + totalQuan + '\'' + 
			",colorISN = '" + colorISN + '\'' + 
			",storeGroup2Name = '" + storeGroup2Name + '\'' + 
			"}";
		}
}