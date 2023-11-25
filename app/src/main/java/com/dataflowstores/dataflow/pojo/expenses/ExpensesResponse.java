package com.dataflowstores.dataflow.pojo.expenses;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ExpensesResponse implements Serializable {

	@SerializedName("data")
	private List<ExpenseData> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(List<ExpenseData> data){
		this.data = data;
	}

	public List<ExpenseData> getData(){
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
			"ExpensesResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}