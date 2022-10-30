package com.dataflowstores.dataflow.pojo.expenses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExpenseData implements Serializable {

	@SerializedName("TaxeCardNo")
	private String taxeCardNo;

	@SerializedName("DealerBranchISN")
	private String dealerBranchISN;

	@SerializedName("BranchAddress")
	private String branchAddress;

	@SerializedName("ShiftISN")
	private String shiftISN;

	@SerializedName("CreateDate")
	private String createDate;

	@SerializedName("ModifyDate")
	private String modifyDate;

	@SerializedName("HeaderNotes")
	private String headerNotes;

	@SerializedName("BranchName")
	private String branchName;

	@SerializedName("DealerISN")
	private String dealerISN;

	@SerializedName("BranchISN")
	private String branchISN;

	@SerializedName("WorkingDayDate")
	private String workingDayDate;

	@SerializedName("CashTypeName")
	private String cashTypeName;

	@SerializedName("ShiftName")
	private String shiftName;

	@SerializedName("ShiftBranchISN")
	private String shiftBranchISN;

	@SerializedName("SelectedWorkerName")
	private String selectedWorkerName;

	@SerializedName("TradeRecoredNo")
	private String tradeRecoredNo;

	@SerializedName("WorkerName")
	private String workerName;

	@SerializedName("SubExpMenuName")
	private String subExpMenuName;

	@SerializedName("DealerType")
	private String dealerType;

	@SerializedName("Tel2")
	private String tel2;

	@SerializedName("Tel1")
	private String tel1;

	@SerializedName("Move_ISN")
	private String moveISN;

	@SerializedName("NetValue")
	private String netValue;

	@SerializedName("ExpMenuName")
	private String expMenuName;

	@SerializedName("Move_ID")
	private String moveID;

	public void setTaxeCardNo(String taxeCardNo){
		this.taxeCardNo = taxeCardNo;
	}

	public String getTaxeCardNo(){
		return taxeCardNo;
	}

	public void setDealerBranchISN(String dealerBranchISN){
		this.dealerBranchISN = dealerBranchISN;
	}

	public String getDealerBranchISN(){
		return dealerBranchISN;
	}

	public void setBranchAddress(String branchAddress){
		this.branchAddress = branchAddress;
	}

	public String getBranchAddress(){
		return branchAddress;
	}

	public void setShiftISN(String shiftISN){
		this.shiftISN = shiftISN;
	}

	public String getShiftISN(){
		return shiftISN;
	}

	public void setCreateDate(String createDate){
		this.createDate = createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setModifyDate(String modifyDate){
		this.modifyDate = modifyDate;
	}

	public String getModifyDate(){
		return modifyDate;
	}

	public void setHeaderNotes(String headerNotes){
		this.headerNotes = headerNotes;
	}

	public String getHeaderNotes(){
		return headerNotes;
	}

	public void setBranchName(String branchName){
		this.branchName = branchName;
	}

	public String getBranchName(){
		return branchName;
	}

	public void setDealerISN(String dealerISN){
		this.dealerISN = dealerISN;
	}

	public String getDealerISN(){
		return dealerISN;
	}

	public void setBranchISN(String branchISN){
		this.branchISN = branchISN;
	}

	public String getBranchISN(){
		return branchISN;
	}

	public void setWorkingDayDate(String workingDayDate){
		this.workingDayDate = workingDayDate;
	}

	public String getWorkingDayDate(){
		return workingDayDate;
	}

	public void setCashTypeName(String cashTypeName){
		this.cashTypeName = cashTypeName;
	}

	public String getCashTypeName(){
		return cashTypeName;
	}

	public void setShiftName(String shiftName){
		this.shiftName = shiftName;
	}

	public String getShiftName(){
		return shiftName;
	}

	public void setShiftBranchISN(String shiftBranchISN){
		this.shiftBranchISN = shiftBranchISN;
	}

	public String getShiftBranchISN(){
		return shiftBranchISN;
	}

	public void setSelectedWorkerName(String selectedWorkerName){
		this.selectedWorkerName = selectedWorkerName;
	}

	public String getSelectedWorkerName(){
		return selectedWorkerName;
	}

	public void setTradeRecoredNo(String tradeRecoredNo){
		this.tradeRecoredNo = tradeRecoredNo;
	}

	public String getTradeRecoredNo(){
		return tradeRecoredNo;
	}

	public void setWorkerName(String workerName){
		this.workerName = workerName;
	}

	public String getWorkerName(){
		return workerName;
	}

	public void setSubExpMenuName(String subExpMenuName){
		this.subExpMenuName = subExpMenuName;
	}

	public String getSubExpMenuName(){
		return subExpMenuName;
	}

	public void setDealerType(String dealerType){
		this.dealerType = dealerType;
	}

	public String getDealerType(){
		return dealerType;
	}

	public void setTel2(String tel2){
		this.tel2 = tel2;
	}

	public String getTel2(){
		return tel2;
	}

	public void setTel1(String tel1){
		this.tel1 = tel1;
	}

	public String getTel1(){
		return tel1;
	}

	public void setMoveISN(String moveISN){
		this.moveISN = moveISN;
	}

	public String getMoveISN(){
		return moveISN;
	}

	public void setNetValue(String netValue){
		this.netValue = netValue;
	}

	public String getNetValue(){
		return netValue;
	}

	public void setExpMenuName(String expMenuName){
		this.expMenuName = expMenuName;
	}

	public String getExpMenuName(){
		return expMenuName;
	}

	public void setMoveID(String moveID){
		this.moveID = moveID;
	}

	public String getMoveID(){
		return moveID;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"taxeCardNo = '" + taxeCardNo + '\'' + 
			",dealerBranchISN = '" + dealerBranchISN + '\'' + 
			",branchAddress = '" + branchAddress + '\'' + 
			",shiftISN = '" + shiftISN + '\'' + 
			",createDate = '" + createDate + '\'' + 
			",modifyDate = '" + modifyDate + '\'' + 
			",headerNotes = '" + headerNotes + '\'' + 
			",branchName = '" + branchName + '\'' + 
			",dealerISN = '" + dealerISN + '\'' + 
			",branchISN = '" + branchISN + '\'' + 
			",workingDayDate = '" + workingDayDate + '\'' + 
			",cashTypeName = '" + cashTypeName + '\'' + 
			",shiftName = '" + shiftName + '\'' + 
			",shiftBranchISN = '" + shiftBranchISN + '\'' + 
			",selectedWorkerName = '" + selectedWorkerName + '\'' + 
			",tradeRecoredNo = '" + tradeRecoredNo + '\'' + 
			",workerName = '" + workerName + '\'' + 
			",subExpMenuName = '" + subExpMenuName + '\'' + 
			",dealerType = '" + dealerType + '\'' + 
			",tel2 = '" + tel2 + '\'' + 
			",tel1 = '" + tel1 + '\'' + 
			",move_ISN = '" + moveISN + '\'' + 
			",netValue = '" + netValue + '\'' + 
			",expMenuName = '" + expMenuName + '\'' + 
			",move_ID = '" + moveID + '\'' + 
			"}";
		}
}