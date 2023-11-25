package com.dataflowstores.dataflow.pojo.report.cashierMoves;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CashierMoveData implements Serializable {

	@SerializedName("WorkingDayDate")
	private String workingDayDate;

	@SerializedName("MoveName")
	private String moveName;

	@SerializedName("NetValue")
	private String netValue;

	@SerializedName("CreateDate")
	private String createDate;

	@SerializedName("DealerName")
	private String dealerName;

	@SerializedName("Move_ID")
	private String moveID;

	@SerializedName("MoveType")
	private String MoveType;
	@SerializedName("BranchISN")
	private String BranchISN;
	@SerializedName("WorkerCBranchISN")
	private String WorkerCBranchISN;
	@SerializedName("WorkerCISN")
	private String WorkerCISN;

	public void setWorkingDayDate(String workingDayDate) {
		this.workingDayDate = workingDayDate;
	}

	public void setMoveName(String moveName) {
		this.moveName = moveName;
	}

	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public void setMoveID(String moveID) {
		this.moveID = moveID;
	}

	public String getMoveType() {
		return MoveType;
	}

	public void setMoveType(String moveType) {
		MoveType = moveType;
	}

	public String getBranchISN() {
		return BranchISN;
	}

	public void setBranchISN(String branchISN) {
		BranchISN = branchISN;
	}

	public String getWorkerCBranchISN() {
		return WorkerCBranchISN;
	}

	public void setWorkerCBranchISN(String workerCBranchISN) {
		WorkerCBranchISN = workerCBranchISN;
	}

	public String getWorkerCISN() {
		return WorkerCISN;
	}

	public void setWorkerCISN(String workerCISN) {
		WorkerCISN = workerCISN;
	}

	public String getWorkingDayDate(){
		return workingDayDate;
	}

	public String getMoveName(){
		return moveName;
	}

	public String getNetValue(){
		return netValue;
	}

	public String getCreateDate(){
		return createDate;
	}

	public String getDealerName(){
		return dealerName;
	}

	public String getMoveID(){
		return moveID;
	}
}