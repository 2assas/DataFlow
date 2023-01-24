package com.dataflowstores.dataflow.pojo.report.itemSalesReport;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ItemSalesResponse implements Serializable {

	@SerializedName("data")
	private List<ItemSalesData> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public List<ItemSalesData> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}