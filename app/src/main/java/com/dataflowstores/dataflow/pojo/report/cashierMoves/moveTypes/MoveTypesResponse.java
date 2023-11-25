package com.dataflowstores.dataflow.pojo.report.cashierMoves.moveTypes;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MoveTypesResponse{

	@SerializedName("data")
	private List<MoveType> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public List<MoveType> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}