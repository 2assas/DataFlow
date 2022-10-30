package com.dataflowstores.dataflow.pojo.expenses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SubExpResponse{

	@SerializedName("data")
	private List<SubExpItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(List<SubExpItem> data){
		this.data = data;
	}

	public List<SubExpItem> getData(){
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
			"SubExpResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}