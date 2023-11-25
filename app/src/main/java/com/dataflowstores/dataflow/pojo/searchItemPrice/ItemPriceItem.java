package com.dataflowstores.dataflow.pojo.searchItemPrice;

import com.google.gson.annotations.SerializedName;

public class ItemPriceItem {

	@SerializedName("BasicUnitQuantity")
	private String basicUnitQuantity;

	@SerializedName("Price")
	private String price;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("PricesTypeName")
	private String pricesTypeName;

	@SerializedName("MeasureUnitArName")
	private String measureUnitArName;

	public void setBasicUnitQuantity(String basicUnitQuantity){
		this.basicUnitQuantity = basicUnitQuantity;
	}

	public String getBasicUnitQuantity(){
		return basicUnitQuantity;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setPricesTypeName(String pricesTypeName){
		this.pricesTypeName = pricesTypeName;
	}

	public String getPricesTypeName(){
		return pricesTypeName;
	}

	public void setMeasureUnitArName(String measureUnitArName){
		this.measureUnitArName = measureUnitArName;
	}

	public String getMeasureUnitArName(){
		return measureUnitArName;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"basicUnitQuantity = '" + basicUnitQuantity + '\'' + 
			",price = '" + price + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",pricesTypeName = '" + pricesTypeName + '\'' + 
			",measureUnitArName = '" + measureUnitArName + '\'' + 
			"}";
		}
}