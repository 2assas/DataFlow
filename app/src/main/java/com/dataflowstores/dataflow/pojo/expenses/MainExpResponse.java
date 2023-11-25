package com.dataflowstores.dataflow.pojo.expenses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MainExpResponse{

	@SerializedName("data")
	private List<MainExpItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(List<MainExpItem> data){
		this.data = data;
	}

	public List<MainExpItem> getData(){
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
			"MainExpResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}