package com.dataflowstores.dataflow.pojo.financialReport;

import com.google.gson.annotations.SerializedName;

public class ReportItem{

	@SerializedName("SaleType")
	private String saleType;

	@SerializedName("Hospitality")
	private String hospitality;

	@SerializedName("Serial")
	private int serial;

	@SerializedName("Type")
	private String type;

	@SerializedName("Reservation")
	private String reservation;

	@SerializedName("AccountValue")
	private double accountValue;

	@SerializedName("CashType")
	private String cashType;

	@SerializedName("NetValue")
	private double netValue;

	@SerializedName("MoveType")
	private String moveType;

	@SerializedName("AccountCount")
	private String accountCount;

	@SerializedName("AccountName")
	private String accountName;

	@SerializedName("Officer")
	private String officer;

	public void setSaleType(String saleType){
		this.saleType = saleType;
	}

	public String getSaleType(){
		return saleType;
	}

	public void setHospitality(String hospitality){
		this.hospitality = hospitality;
	}

	public String getHospitality(){
		return hospitality;
	}

	public void setSerial(int serial){
		this.serial = serial;
	}

	public int getSerial(){
		return serial;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setReservation(String reservation){
		this.reservation = reservation;
	}

	public String getReservation(){
		return reservation;
	}

	public void setAccountValue(double accountValue){
		this.accountValue = accountValue;
	}

	public double getAccountValue(){
		return accountValue;
	}

	public void setCashType(String cashType){
		this.cashType = cashType;
	}

	public String getCashType(){
		return cashType;
	}

	public void setNetValue(double netValue){
		this.netValue = netValue;
	}

	public double getNetValue(){
		return netValue;
	}

	public void setMoveType(String moveType){
		this.moveType = moveType;
	}

	public String getMoveType(){
		return moveType;
	}

	public void setAccountCount(String accountCount){
		this.accountCount = accountCount;
	}

	public String getAccountCount(){
		return accountCount;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setOfficer(String officer){
		this.officer = officer;
	}

	public String getOfficer(){
		return officer;
	}
}