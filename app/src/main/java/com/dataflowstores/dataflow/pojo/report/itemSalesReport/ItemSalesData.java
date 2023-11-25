package com.dataflowstores.dataflow.pojo.report.itemSalesReport;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemSalesData implements Serializable {

	@SerializedName("TCount")
	private String tCount;

	@SerializedName("quantity_description")
	private String quantityDescription;

	@SerializedName("MoveName")
	private String moveName;

	@SerializedName("NetPrice")
	private String netPrice;

	@SerializedName("ItemBranchISN")
	private String itemBranchISN;

	@SerializedName("MeasureUnitBranchISN")
	private String measureUnitBranchISN;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("MoveType")
	private String moveType;

	@SerializedName("MeasureUnitISN")
	private String measureUnitISN;

	@SerializedName("ItemISN")
	private String itemISN;

	@SerializedName("TotalQuan")
	private double totalQuan;

	public String getTCount(){
		return tCount;
	}

	public String getQuantityDescription(){
		return quantityDescription;
	}

	public String getMoveName(){
		return moveName;
	}

	public String getNetPrice(){
		return netPrice;
	}

	public String getItemBranchISN(){
		return itemBranchISN;
	}

	public String getMeasureUnitBranchISN(){
		return measureUnitBranchISN;
	}

	public String getItemName(){
		return itemName;
	}

	public String getMoveType(){
		return moveType;
	}

	public String getMeasureUnitISN(){
		return measureUnitISN;
	}

	public String getItemISN(){
		return itemISN;
	}

	public double getTotalQuan(){
		return totalQuan;
	}
}