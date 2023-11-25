package com.dataflowstores.dataflow.pojo.financialReport;

import com.google.gson.annotations.SerializedName;

public class ReportBody{

	@SerializedName("payment_methods")
	private PaymentMethods paymentMethods;

	@SerializedName("Shift_ISN")
	private String shiftISN;

	@SerializedName("from_workday")
	private String fromWorkday;

	@SerializedName("BankBranchISN")
	private String bankBranchISN;

	@SerializedName("Branch_ISN")
	private Long branchISN;

	@SerializedName("to_workday")
	private String toWorkday;

	@SerializedName("Bank_ISN")
	private String bankISN;

	@SerializedName("SafeDeposit_ISN")
	private String SafeDeposit_ISN;

	@SerializedName("SafeDepositBranchISN")
	private String safeDepositBranchISN;

	@SerializedName("Worker_ISN")
	private String Worker_ISN;


	@SerializedName("from")
	private String from;


	@SerializedName("to")
	private String to;


	public String getSafeDepositBranchISN() {
		return safeDepositBranchISN;
	}

	public void setSafeDepositBranchISN(String safeDepositBranchISN) {
		this.safeDepositBranchISN = safeDepositBranchISN;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setBranchISN(Long branchISN) {
		this.branchISN = branchISN;
	}

	public String getSafeDeposit_ISN() {
		return SafeDeposit_ISN;
	}

	public void setSafeDeposit_ISN(String safeDeposit_ISN) {
		SafeDeposit_ISN = safeDeposit_ISN;
	}

	public String getWorker_ISN() {
		return Worker_ISN;
	}

	public void setWorker_ISN(String worker_ISN) {
		Worker_ISN = worker_ISN;
	}

	public void setPaymentMethods(PaymentMethods paymentMethods){
		this.paymentMethods = paymentMethods;
	}

	public PaymentMethods getPaymentMethods(){
		return paymentMethods;
	}

	public void setShiftISN(String shiftISN){
		this.shiftISN = shiftISN;
	}

	public String getShiftISN(){
		return shiftISN;
	}

	public void setFromWorkday(String fromWorkday){
		this.fromWorkday = fromWorkday;
	}

	public String getFromWorkday(){
		return fromWorkday;
	}

	public void setBankBranchISN(String bankBranchISN){
		this.bankBranchISN = bankBranchISN;
	}

	public String getBankBranchISN(){
		return bankBranchISN;
	}

	public void setBranchISN(long branchISN){
		this.branchISN = branchISN;
	}

	public long getBranchISN(){
		return branchISN;
	}

	public void setToWorkday(String toWorkday){
		this.toWorkday = toWorkday;
	}

	public String getToWorkday(){
		return toWorkday;
	}

	public void setBankISN(String bankISN){
		this.bankISN = bankISN;
	}

	public String getBankISN(){
		return bankISN;
	}
}