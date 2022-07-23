package com.example.dataflow.pojo.invoice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoveHeader implements Serializable {

    @SerializedName("BillNumber")
    String BillNumber="";
    @SerializedName("BillNumberIntNumber")
    String BillNumberIntNumber="";
    @SerializedName("MoveNumber")
    String MoveNumber="";
    @SerializedName("Move_ID")
    String Move_ID="";
    @SerializedName("MoveDate")
    String MoveDate="";
    @SerializedName("TotalLinesValue")
    String TotalLinesValue="";
    @SerializedName("TotalLinesValueAfterDisc")
    String TotalLinesValueAfterDisc="";
    @SerializedName("ServiceValue")
    String ServiceValue="";
    @SerializedName("ServicePer")
    String ServicePer="";
    @SerializedName("DeliveryValue")
    String DeliveryValue="";
    @SerializedName("TotalValueAfterServices")
    String TotalValueAfterServices="";
    @SerializedName("BasicDiscountVal")
    String BasicDiscountVal="";
    @SerializedName("BasicDiscountPer")
    String BasicDiscountPer="";
    @SerializedName("TotalValueAfterDisc")
    String TotalValueAfterDisc="";
    @SerializedName("TotalLinesTaxVal")
    String TotalLinesTaxVal="";
    @SerializedName("BasicTaxVal")
    String BasicTaxVal="";
    @SerializedName("BasicTaxPer")
    String BasicTaxPer="";
    @SerializedName("TotalValueAfterTax")
    String TotalValueAfterTax="";
    @SerializedName("NetValue")
    String NetValue="";
    @SerializedName("PaidValue")
    String PaidValue="";
    @SerializedName("RemainValue")
    String RemainValue="";
    @SerializedName("TableNumber")
    String TableNumber="";
    @SerializedName("DeliveryPhone")
    String DeliveryPhone="";
    @SerializedName("DeliveryAddress")
    String DeliveryAddress="";
    @SerializedName("ItemName")
    String ItemName="";
    @SerializedName("WorkerName")
    String WorkerName="";
    @SerializedName("ShiftName")
    String ShiftName="";
    @SerializedName("TQ")
    String TQ="";
    @SerializedName("ShiftBranchISN")
    String ShiftBranchISN="";
    @SerializedName("ShiftISN")
    String ShiftISN="";
    @SerializedName("BranchISN")
    String BranchISN="";
    @SerializedName("Move_ISN")
    String Move_ISN="";
    @SerializedName("CreateDate")
    String CreateDate="";
    @SerializedName("ModifyDate")
    String ModifyDate="";
    @SerializedName("WorkingDayDate")
    String WorkingDayDate="";
    @SerializedName("DealerName")
    String DealerName="";
    @SerializedName("SaleTypeName")
    String SaleTypeName="";
    @SerializedName("HeaderNotes")
    String HeaderNotes="";
    @SerializedName("ShowWorkerName")
    String ShowWorkerName="";
    @SerializedName("ShowMove_ID")
    String ShowMove_ID="";
    @SerializedName("ShowSaleType")
    String ShowSaleType="";
    @SerializedName("HandingDateTime")
    String HandingDateTime="";
    @SerializedName("ReservationDateTime")
    String ReservationDateTime="";
    @SerializedName("Reservation")
    String Reservation="";
    @SerializedName("ChooseHandingDateTime")
    String ChooseHandingDateTime="";
    @SerializedName("ShowItemsCount")
    String ShowItemsCount="";
    @SerializedName("ShowCashierBillIntNumber")
    String ShowCashierBillIntNumber="";
    @SerializedName("CashierBillNumberRestart")
    String CashierBillNumberRestart="";
    @SerializedName("ShowCashierBillIntNumberU")
    String ShowCashierBillIntNumberU="";
    @SerializedName("DiscVal1")
    String DiscVal1="";
    @SerializedName("CashierLinesDiscountActivate")
    String CashierLinesDiscountActivate="";
    @SerializedName("RandomBillNumber")
    String RandomBillNumber="";
    @SerializedName("CashierRandomBillNumber")
    String CashierRandomBillNumber="";
    @SerializedName("CheckTimeOnly")
    String CheckTimeOnly="";
    @SerializedName("DeliveryManName")
    String DeliveryManName="";
    @SerializedName("OtlobCompanyBranchISN")
    String OtlobCompanyBranchISN="";
    @SerializedName("OtlobCompanyISN")
    String OtlobCompanyISN="";
    @SerializedName("OtlobCompanyName")
    String OtlobCompanyName="";
    @SerializedName("ExpectedDeliveryTime")
    String ExpectedDeliveryTime="";
    @SerializedName("ExpectedDeliveryTimeNotes")
    String ExpectedDeliveryTimeNotes="";
    @SerializedName("OtlobCheckNumber")
    String OtlobCheckNumber="";
    @SerializedName("SaleWorker")
    String SaleWorker="";
    @SerializedName("CashTypeName")
    String CashTypeName="";
    @SerializedName("Tel1")
    String Tel1="";
    @SerializedName("Tel2")
    String Tel2="";
    @SerializedName("BranchAddress")
    String BranchAddress="";
    @SerializedName("BranchName")
    String BranchName="";
    @SerializedName("TradeRecoredNo")
    String TradeRecoredNo="";
    @SerializedName("TaxeCardNo")
    String TaxeCardNo="";
    @SerializedName("SaleManName")
    String SaleManName="";

    public String getSaleManName() {
        return SaleManName;
    }

    public void setSaleManName(String saleManName) {
        SaleManName = saleManName;
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

    public String getBillNumber() {
        return BillNumber;
    }

    public void setBillNumber(String billNumber) {
        BillNumber = billNumber;
    }

    public String getBillNumberIntNumber() {
        return BillNumberIntNumber;
    }

    public void setBillNumberIntNumber(String billNumberIntNumber) {
        BillNumberIntNumber = billNumberIntNumber;
    }

    public String getMoveNumber() {
        return MoveNumber;
    }

    public void setMoveNumber(String moveNumber) {
        MoveNumber = moveNumber;
    }

    public String getMove_ID() {
        return Move_ID;
    }

    public void setMove_ID(String move_ID) {
        Move_ID = move_ID;
    }

    public String getMoveDate() {
        return MoveDate;
    }

    public void setMoveDate(String moveDate) {
        MoveDate = moveDate;
    }

    public String getTotalLinesValue() {
        return TotalLinesValue;
    }

    public void setTotalLinesValue(String totalLinesValue) {
        TotalLinesValue = totalLinesValue;
    }

    public String getTotalLinesValueAfterDisc() {
        return TotalLinesValueAfterDisc;
    }

    public void setTotalLinesValueAfterDisc(String totalLinesValueAfterDisc) {
        TotalLinesValueAfterDisc = totalLinesValueAfterDisc;
    }

    public String getServiceValue() {
        return ServiceValue;
    }

    public void setServiceValue(String serviceValue) {
        ServiceValue = serviceValue;
    }

    public String getServicePer() {
        return ServicePer;
    }

    public void setServicePer(String servicePer) {
        ServicePer = servicePer;
    }

    public String getDeliveryValue() {
        return DeliveryValue;
    }

    public void setDeliveryValue(String deliveryValue) {
        DeliveryValue = deliveryValue;
    }

    public String getTotalValueAfterServices() {
        return TotalValueAfterServices;
    }

    public void setTotalValueAfterServices(String totalValueAfterServices) {
        TotalValueAfterServices = totalValueAfterServices;
    }

    public String getBasicDiscountVal() {
        return BasicDiscountVal;
    }

    public void setBasicDiscountVal(String basicDiscountVal) {
        BasicDiscountVal = basicDiscountVal;
    }

    public String getBasicDiscountPer() {
        return BasicDiscountPer;
    }

    public void setBasicDiscountPer(String basicDiscountPer) {
        BasicDiscountPer = basicDiscountPer;
    }

    public String getTotalValueAfterDisc() {
        return TotalValueAfterDisc;
    }

    public void setTotalValueAfterDisc(String totalValueAfterDisc) {
        TotalValueAfterDisc = totalValueAfterDisc;
    }

    public String getTotalLinesTaxVal() {
        return TotalLinesTaxVal;
    }

    public void setTotalLinesTaxVal(String totalLinesTaxVal) {
        TotalLinesTaxVal = totalLinesTaxVal;
    }

    public String getBasicTaxVal() {
        return BasicTaxVal;
    }

    public void setBasicTaxVal(String basicTaxVal) {
        BasicTaxVal = basicTaxVal;
    }

    public String getBasicTaxPer() {
        return BasicTaxPer;
    }

    public void setBasicTaxPer(String basicTaxPer) {
        BasicTaxPer = basicTaxPer;
    }

    public String getTotalValueAfterTax() {
        return TotalValueAfterTax;
    }

    public void setTotalValueAfterTax(String totalValueAfterTax) {
        TotalValueAfterTax = totalValueAfterTax;
    }

    public String getNetValue() {
        return NetValue;
    }

    public void setNetValue(String netValue) {
        NetValue = netValue;
    }

    public String getPaidValue() {
        return PaidValue;
    }

    public void setPaidValue(String paidValue) {
        PaidValue = paidValue;
    }

    public String getRemainValue() {
        return RemainValue;
    }

    public void setRemainValue(String remainValue) {
        RemainValue = remainValue;
    }

    public String getTableNumber() {
        return TableNumber;
    }

    public void setTableNumber(String tableNumber) {
        TableNumber = tableNumber;
    }

    public String getDeliveryPhone() {
        return DeliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        DeliveryPhone = deliveryPhone;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
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

    public String getTQ() {
        return TQ;
    }

    public void setTQ(String TQ) {
        this.TQ = TQ;
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

    public String getSaleTypeName() {
        return SaleTypeName;
    }

    public void setSaleTypeName(String saleTypeName) {
        SaleTypeName = saleTypeName;
    }

    public String getHeaderNotes() {
        return HeaderNotes;
    }

    public void setHeaderNotes(String headerNotes) {
        HeaderNotes = headerNotes;
    }

    public String getShowWorkerName() {
        return ShowWorkerName;
    }

    public void setShowWorkerName(String showWorkerName) {
        ShowWorkerName = showWorkerName;
    }

    public String getShowMove_ID() {
        return ShowMove_ID;
    }

    public void setShowMove_ID(String showMove_ID) {
        ShowMove_ID = showMove_ID;
    }

    public String getShowSaleType() {
        return ShowSaleType;
    }

    public void setShowSaleType(String showSaleType) {
        ShowSaleType = showSaleType;
    }

    public String getHandingDateTime() {
        return HandingDateTime;
    }

    public void setHandingDateTime(String handingDateTime) {
        HandingDateTime = handingDateTime;
    }

    public String getReservationDateTime() {
        return ReservationDateTime;
    }

    public void setReservationDateTime(String reservationDateTime) {
        ReservationDateTime = reservationDateTime;
    }

    public String getReservation() {
        return Reservation;
    }

    public void setReservation(String reservation) {
        Reservation = reservation;
    }

    public String getChooseHandingDateTime() {
        return ChooseHandingDateTime;
    }

    public void setChooseHandingDateTime(String chooseHandingDateTime) {
        ChooseHandingDateTime = chooseHandingDateTime;
    }

    public String getShowItemsCount() {
        return ShowItemsCount;
    }

    public void setShowItemsCount(String showItemsCount) {
        ShowItemsCount = showItemsCount;
    }

    public String getShowCashierBillIntNumber() {
        return ShowCashierBillIntNumber;
    }

    public void setShowCashierBillIntNumber(String showCashierBillIntNumber) {
        ShowCashierBillIntNumber = showCashierBillIntNumber;
    }

    public String getCashierBillNumberRestart() {
        return CashierBillNumberRestart;
    }

    public void setCashierBillNumberRestart(String cashierBillNumberRestart) {
        CashierBillNumberRestart = cashierBillNumberRestart;
    }

    public String getShowCashierBillIntNumberU() {
        return ShowCashierBillIntNumberU;
    }

    public void setShowCashierBillIntNumberU(String showCashierBillIntNumberU) {
        ShowCashierBillIntNumberU = showCashierBillIntNumberU;
    }

    public String getDiscVal1() {
        return DiscVal1;
    }

    public void setDiscVal1(String discVal1) {
        DiscVal1 = discVal1;
    }

    public String getCashierLinesDiscountActivate() {
        return CashierLinesDiscountActivate;
    }

    public void setCashierLinesDiscountActivate(String cashierLinesDiscountActivate) {
        CashierLinesDiscountActivate = cashierLinesDiscountActivate;
    }

    public String getRandomBillNumber() {
        return RandomBillNumber;
    }

    public void setRandomBillNumber(String randomBillNumber) {
        RandomBillNumber = randomBillNumber;
    }

    public String getCashierRandomBillNumber() {
        return CashierRandomBillNumber;
    }

    public void setCashierRandomBillNumber(String cashierRandomBillNumber) {
        CashierRandomBillNumber = cashierRandomBillNumber;
    }

    public String getCheckTimeOnly() {
        return CheckTimeOnly;
    }

    public void setCheckTimeOnly(String checkTimeOnly) {
        CheckTimeOnly = checkTimeOnly;
    }

    public String getDeliveryManName() {
        return DeliveryManName;
    }

    public void setDeliveryManName(String deliveryManName) {
        DeliveryManName = deliveryManName;
    }

    public String getOtlobCompanyBranchISN() {
        return OtlobCompanyBranchISN;
    }

    public void setOtlobCompanyBranchISN(String otlobCompanyBranchISN) {
        OtlobCompanyBranchISN = otlobCompanyBranchISN;
    }

    public String getOtlobCompanyISN() {
        return OtlobCompanyISN;
    }

    public void setOtlobCompanyISN(String otlobCompanyISN) {
        OtlobCompanyISN = otlobCompanyISN;
    }

    public String getOtlobCompanyName() {
        return OtlobCompanyName;
    }

    public void setOtlobCompanyName(String otlobCompanyName) {
        OtlobCompanyName = otlobCompanyName;
    }

    public String getExpectedDeliveryTime() {
        return ExpectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(String expectedDeliveryTime) {
        ExpectedDeliveryTime = expectedDeliveryTime;
    }

    public String getExpectedDeliveryTimeNotes() {
        return ExpectedDeliveryTimeNotes;
    }

    public void setExpectedDeliveryTimeNotes(String expectedDeliveryTimeNotes) {
        ExpectedDeliveryTimeNotes = expectedDeliveryTimeNotes;
    }

    public String getOtlobCheckNumber() {
        return OtlobCheckNumber;
    }

    public void setOtlobCheckNumber(String otlobCheckNumber) {
        OtlobCheckNumber = otlobCheckNumber;
    }

    public String getSaleWorker() {
        return SaleWorker;
    }

    public void setSaleWorker(String saleWorker) {
        SaleWorker = saleWorker;
    }
}
