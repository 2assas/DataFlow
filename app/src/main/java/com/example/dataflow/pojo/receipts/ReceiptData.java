package com.example.dataflow.pojo.receipts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiptData implements Serializable {
   @SerializedName("Move_ID")
   String moveId;
   @SerializedName("NetValue")
   String NetValue;
   @SerializedName("WorkerName")
   String WorkerName;
   @SerializedName("ShiftName")
   String ShiftName;
   @SerializedName("ShiftBranchISN")
   String ShiftBranchISN;
   @SerializedName("ShiftISN")
   String ShiftISN;
   @SerializedName("BranchISN")
   String BranchISN;
   @SerializedName("Move_ISN")
   String Move_ISN;
   @SerializedName("CreateDate")
   String CreateDate;
   @SerializedName("ModifyDate")
   String ModifyDate;
   @SerializedName("WorkingDayDate")
   String WorkingDayDate;
   @SerializedName("DealerName")
   String DealerName;
   @SerializedName("HeaderNotes")
   String HeaderNotes;
   @SerializedName("CashTypeName")
   String CashTypeName;
   @SerializedName("Tel1")
   String Tel1;
   @SerializedName("Tel2")
   String Tel2;
   @SerializedName("BranchAddress")
   String BranchAddress;
   @SerializedName("BranchName")
   String BranchName;
   @SerializedName("TradeRecoredNo")
   String TradeRecoredNo;
   @SerializedName("TaxeCardNo")
   String TaxeCardNo;
   @SerializedName("SaleManName")
   String SaleManName;

    public String getMoveId() {
        return moveId;
    }

    public void setMoveId(String moveId) {
        this.moveId = moveId;
    }

    public String getNetValue() {
        return NetValue;
    }

    public void setNetValue(String netValue) {
        NetValue = netValue;
    }

    public String getWorkerName() {
        return WorkerName;
    }

    public void setWorkerName(String workerName) {
        WorkerName = workerName;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getShiftBranchISN() {
        return ShiftBranchISN;
    }

    public void setShiftBranchISN(String shiftBranchISN) {
        ShiftBranchISN = shiftBranchISN;
    }

    public String getShiftISN() {
        return ShiftISN;
    }

    public void setShiftISN(String shiftISN) {
        ShiftISN = shiftISN;
    }

    public String getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(String branchISN) {
        BranchISN = branchISN;
    }

    public String getMove_ISN() {
        return Move_ISN;
    }

    public void setMove_ISN(String move_ISN) {
        Move_ISN = move_ISN;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getWorkingDayDate() {
        return WorkingDayDate;
    }

    public void setWorkingDayDate(String workingDayDate) {
        WorkingDayDate = workingDayDate;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getHeaderNotes() {
        return HeaderNotes;
    }

    public void setHeaderNotes(String headerNotes) {
        HeaderNotes = headerNotes;
    }

    public String getCashTypeName() {
        return CashTypeName;
    }

    public void setCashTypeName(String cashTypeName) {
        CashTypeName = cashTypeName;
    }

    public String getTel1() {
        return Tel1;
    }

    public void setTel1(String tel1) {
        Tel1 = tel1;
    }

    public String getTel2() {
        return Tel2;
    }

    public void setTel2(String tel2) {
        Tel2 = tel2;
    }

    public String getBranchAddress() {
        return BranchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        BranchAddress = branchAddress;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getTradeRecoredNo() {
        return TradeRecoredNo;
    }

    public void setTradeRecoredNo(String tradeRecoredNo) {
        TradeRecoredNo = tradeRecoredNo;
    }

    public String getTaxeCardNo() {
        return TaxeCardNo;
    }

    public void setTaxeCardNo(String taxeCardNo) {
        TaxeCardNo = taxeCardNo;
    }

    public String getSaleManName() {
        return SaleManName;
    }

    public void setSaleManName(String saleManName) {
        SaleManName = saleManName;
    }
}
