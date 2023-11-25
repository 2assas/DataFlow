package com.dataflowstores.dataflow.pojo.expenses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class WorkerResponse{

	@SerializedName("data")
	private List<WorkerItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(List<WorkerItem> data){
		this.data = data;
	}

	public List<WorkerItem> getData(){
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
			"WorkerResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}