package com.dataflowstores.dataflow.pojo.financialReport;

import com.google.gson.annotations.SerializedName;

public class Shift{

	@SerializedName("Shift_Status")
	private String shiftStatus;

	@SerializedName("Shift_Close_Date")
	private String shiftCloseDate;

	@SerializedName("Close_Cash")
	private String closeCash;

	@SerializedName("LogInPass")
	private String logInPass;

	@SerializedName("CreateSource")
	private String createSource;

	@SerializedName("Shift_Open_Date")
	private String shiftOpenDate;

	@SerializedName("Assumed_Cash")
	private double assumedCash;

	@SerializedName("CreateDate")
	private String createDate;

	@SerializedName("Open_Cash")
	private String openCash;

	@SerializedName("Shift_Total")
	private double shiftTotal;

	@SerializedName("WorkerMBranchISN")
	private String workerMBranchISN;

	@SerializedName("OpenedWorkStationISN")
	private String openedWorkStationISN;

	@SerializedName("ModifyDate")
	private String modifyDate;

	@SerializedName("OpenedWinProcessorID")
	private String openedWinProcessorID;

	@SerializedName("ShiftNotes")
	private String shiftNotes;

	@SerializedName("Handed_Cash")
	private String handedCash;

	@SerializedName("WorkingDayDate")
	private String workingDayDate;

	@SerializedName("ShiftName")
	private String shiftName;

	@SerializedName("OpenedWorkStationBranchISN")
	private String openedWorkStationBranchISN;

	@SerializedName("ShiftBranchISN")
	private String shiftBranchISN;

	@SerializedName("OpenedWorkerISN")
	private String openedWorkerISN;

	@SerializedName("OpenedWorkerBranchISN")
	private String openedWorkerBranchISN;

	@SerializedName("Shift_ISN")
	private String shiftISN;

	@SerializedName("WorkerMISN")
	private String workerMISN;

	@SerializedName("MailSent")
	private String mailSent;

	@SerializedName("Create_timestamp")
	private String createTimestamp;

	@SerializedName("Shift_Number")
	private Object shiftNumber;

	@SerializedName("WorkerCBranchISN")
	private String workerCBranchISN;

	@SerializedName("WorkerCISN")
	private String workerCISN;

	public void setShiftStatus(String shiftStatus){
		this.shiftStatus = shiftStatus;
	}

	public String getShiftStatus(){
		return shiftStatus;
	}

	public void setShiftCloseDate(String shiftCloseDate){
		this.shiftCloseDate = shiftCloseDate;
	}

	public String getShiftCloseDate(){
		return shiftCloseDate;
	}

	public void setCloseCash(String closeCash){
		this.closeCash = closeCash;
	}

	public String getCloseCash(){
		return closeCash;
	}

	public void setLogInPass(String logInPass){
		this.logInPass = logInPass;
	}

	public String getLogInPass(){
		return logInPass;
	}

	public void setCreateSource(String createSource){
		this.createSource = createSource;
	}

	public String getCreateSource(){
		return createSource;
	}

	public void setShiftOpenDate(String shiftOpenDate){
		this.shiftOpenDate = shiftOpenDate;
	}

	public String getShiftOpenDate(){
		return shiftOpenDate;
	}

	public void setAssumedCash(double assumedCash){
		this.assumedCash = assumedCash;
	}

	public double getAssumedCash(){
		return assumedCash;
	}

	public void setCreateDate(String createDate){
		this.createDate = createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setOpenCash(String openCash){
		this.openCash = openCash;
	}

	public String getOpenCash(){
		return openCash;
	}

	public void setShiftTotal(double shiftTotal){
		this.shiftTotal = shiftTotal;
	}

	public double getShiftTotal(){
		return shiftTotal;
	}

	public void setWorkerMBranchISN(String workerMBranchISN){
		this.workerMBranchISN = workerMBranchISN;
	}

	public String getWorkerMBranchISN(){
		return workerMBranchISN;
	}

	public void setOpenedWorkStationISN(String openedWorkStationISN){
		this.openedWorkStationISN = openedWorkStationISN;
	}

	public String getOpenedWorkStationISN(){
		return openedWorkStationISN;
	}

	public void setModifyDate(String modifyDate){
		this.modifyDate = modifyDate;
	}

	public String getModifyDate(){
		return modifyDate;
	}

	public void setOpenedWinProcessorID(String openedWinProcessorID){
		this.openedWinProcessorID = openedWinProcessorID;
	}

	public String getOpenedWinProcessorID(){
		return openedWinProcessorID;
	}

	public void setShiftNotes(String shiftNotes){
		this.shiftNotes = shiftNotes;
	}

	public String getShiftNotes(){
		return shiftNotes;
	}

	public void setHandedCash(String handedCash){
		this.handedCash = handedCash;
	}

	public String getHandedCash(){
		return handedCash;
	}

	public void setWorkingDayDate(String workingDayDate){
		this.workingDayDate = workingDayDate;
	}

	public String getWorkingDayDate(){
		return workingDayDate;
	}

	public void setShiftName(String shiftName){
		this.shiftName = shiftName;
	}

	public String getShiftName(){
		return shiftName;
	}

	public void setOpenedWorkStationBranchISN(String openedWorkStationBranchISN){
		this.openedWorkStationBranchISN = openedWorkStationBranchISN;
	}

	public String getOpenedWorkStationBranchISN(){
		return openedWorkStationBranchISN;
	}

	public void setShiftBranchISN(String shiftBranchISN){
		this.shiftBranchISN = shiftBranchISN;
	}

	public String getShiftBranchISN(){
		return shiftBranchISN;
	}

	public void setOpenedWorkerISN(String openedWorkerISN){
		this.openedWorkerISN = openedWorkerISN;
	}

	public String getOpenedWorkerISN(){
		return openedWorkerISN;
	}

	public void setOpenedWorkerBranchISN(String openedWorkerBranchISN){
		this.openedWorkerBranchISN = openedWorkerBranchISN;
	}

	public String getOpenedWorkerBranchISN(){
		return openedWorkerBranchISN;
	}

	public void setShiftISN(String shiftISN){
		this.shiftISN = shiftISN;
	}

	public String getShiftISN(){
		return shiftISN;
	}

	public void setWorkerMISN(String workerMISN){
		this.workerMISN = workerMISN;
	}

	public String getWorkerMISN(){
		return workerMISN;
	}

	public void setMailSent(String mailSent){
		this.mailSent = mailSent;
	}

	public String getMailSent(){
		return mailSent;
	}

	public void setCreateTimestamp(String createTimestamp){
		this.createTimestamp = createTimestamp;
	}

	public String getCreateTimestamp(){
		return createTimestamp;
	}

	public void setShiftNumber(Object shiftNumber){
		this.shiftNumber = shiftNumber;
	}

	public Object getShiftNumber(){
		return shiftNumber;
	}

	public void setWorkerCBranchISN(String workerCBranchISN){
		this.workerCBranchISN = workerCBranchISN;
	}

	public String getWorkerCBranchISN(){
		return workerCBranchISN;
	}

	public void setWorkerCISN(String workerCISN){
		this.workerCISN = workerCISN;
	}

	public String getWorkerCISN(){
		return workerCISN;
	}
}