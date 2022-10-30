package com.dataflowstores.dataflow.pojo.searchItemPrice;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ItemPriceResponse{

	@SerializedName("data")
	private ArrayList<ItemPriceItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(ArrayList<ItemPriceItem> data){
		this.data = data;
	}

	public ArrayList<ItemPriceItem> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ItemPriceResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}