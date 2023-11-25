package com.dataflowstores.dataflow.pojo.report.cashierMoves;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CashierMovesReportResponse implements Serializable {

	@SerializedName("data")
	private List<CashierMoveData> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public List<CashierMoveData> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}