package com.dataflowstores.dataflow.pojo.financialReport;

import com.google.gson.annotations.SerializedName;

public class PaymentMethods{
	public PaymentMethods(int checks, int visa, int cash) {
		this.checks = checks;
		this.visa = visa;
		this.cash = cash;
	}

	@SerializedName("checks")
	private int checks;

	@SerializedName("visa")
	private int visa;

	@SerializedName("cash")
	private int cash;

	public void setChecks(int checks){
		this.checks = checks;
	}

	public int getChecks(){
		return checks;
	}

	public void setVisa(int visa){
		this.visa = visa;
	}

	public int getVisa(){
		return visa;
	}

	public void setCash(int cash){
		this.cash = cash;
	}

	public int getCash(){
		return cash;
	}
}