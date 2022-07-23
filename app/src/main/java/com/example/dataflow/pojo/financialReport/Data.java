package com.example.dataflow.pojo.financialReport;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("shift")
	private Shift shift;

	@SerializedName("finalBalance")
	private double finalBalance;

	@SerializedName("report")
	private List<ReportItem> report;

	@SerializedName("totalExpenses")
	private double totalExpenses;

	@SerializedName("totalRevenue")
	private double totalRevenue;

	public void setShift(Shift shift){
		this.shift = shift;
	}

	public Shift getShift(){
		return shift;
	}

	public void setFinalBalance(double finalBalance){
		this.finalBalance = finalBalance;
	}

	public double getFinalBalance(){
		return finalBalance;
	}

	public void setReport(List<ReportItem> report){
		this.report = report;
	}

	public List<ReportItem> getReport(){
		return report;
	}

	public void setTotalExpenses(double totalExpenses){
		this.totalExpenses = totalExpenses;
	}

	public double getTotalExpenses(){
		return totalExpenses;
	}

	public void setTotalRevenue(double totalRevenue){
		this.totalRevenue = totalRevenue;
	}

	public double getTotalRevenue(){
		return totalRevenue;
	}
}