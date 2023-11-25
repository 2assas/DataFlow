package com.dataflowstores.dataflow.pojo.report.cashierMoves.moveTypes;

import com.google.gson.annotations.SerializedName;

public class MoveType {

	@SerializedName("MoveName")
	private String moveName;

	@SerializedName("MoveType")
	private String moveType;

	public String getMoveName(){
		return moveName;
	}

	public String getMoveType(){
		return moveType;
	}
}