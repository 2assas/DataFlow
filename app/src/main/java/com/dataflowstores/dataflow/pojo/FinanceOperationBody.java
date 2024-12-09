//package com.dataflowstores.dataflow.pojo;
//
//import com.google.gson.annotations.SerializedName;
//
//public class FinanceOperationBody {
//    @SerializedName("mustChooseWorker")
//    private String mustChooseWorker;
//
//    @SerializedName("MainExpMenuISN")
//    private Long mainExpMenuISN;
//
//    @SerializedName("MainExpMenuBranchISN")
//    private Long mainExpMenuBranchISN;
//
//    @SerializedName("MainExpMenuName")
//    private String mainExpMenuName;
//
//    @SerializedName("SubExpMenuISN")
//    private Long subExpMenuISN;
//
//    @SerializedName("SubExpMenuBranchISN")
//    private Long subExpMenuBranchISN;
//
//    @SerializedName("SubExpMenuName")
//    private String subExpMenuName;
//
//    @SerializedName("SelectedWorkerBranchISN")
//    private Long selectedWorkerBranchISN;
//
//    @SerializedName("SelectedWorkerISN")
//    private Long selectedWorkerISN;
//
//    @SerializedName("IllustrativeQuantity")
//    private Integer illustrativeQuantity;
//
//    @SerializedName("DeviceID")
//    private String deviceID;
//
//    @SerializedName("LogIn_CurrentWorkingDayDate")
//    private String logInCurrentWorkingDayDate;
//
//    @SerializedName("VendorID")
//    private Long vendorID;
//
//    @SerializedName("BranchISN")
//    private Long branchISN;
//
//    @SerializedName("uiid")
//    private String uuid;
//
//    @SerializedName("CashType")
//    private int cashType;
//
//    @SerializedName("SaleType")
//    private int saleType;
//
//    @SerializedName("HeaderNotes")
//    private String headerNotes;
//
//    @SerializedName("TotalLinesValue")
//    private double totalLinesValue;
//
//    @SerializedName("ServiceValue")
//    private double serviceValue;
//
//    @SerializedName("ServicePer")
//    private double servicePer;
//
//    @SerializedName("DeliveryValue")
//    private double deliveryValue;
//
//    @SerializedName("TotalValueAfterServices")
//    private double totalValueAfterServices;
//
//    @SerializedName("BasicDiscountVal")
//    private double basicDiscountVal;
//
//    @SerializedName("BasicDiscountPer")
//    private double basicDiscountPer;
//
//    @SerializedName("TotalValueAfterDisc")
//    private double totalValueAfterDisc;
//
//    @SerializedName("BasicTaxVal")
//    private double basicTaxVal;
//
//    @SerializedName("BasicTaxPer")
//    private double basicTaxPer;
//
//    @SerializedName("TotalValueAfterTax")
//    private double totalValueAfterTax;
//
//    @SerializedName("NetValue")
//    private double netValue;
//
//    @SerializedName("PaidValue")
//    private double paidValue;
//
//    @SerializedName("RemainValue")
//    private double remainValue;
//
//    @SerializedName("SafeDepositeBranchISN")
//    private long safeDepositeBranchISN;
//
//    @SerializedName("SafeDepositeISN")
//    private long safeDepositeISN;
//
//    @SerializedName("BankBranchISN")
//    private long bankBranchISN;
//
//    @SerializedName("BankISN")
//    private long bankISN;
//
//    @SerializedName("TableNumber")
//    private String tableNumber;
//
//    @SerializedName("DeliveryPhone")
//    private String deliveryPhone;
//
//    @SerializedName("DeliveryAddress")
//    private String deliveryAddress;
//
//    @SerializedName("WorkerCBranchISN")
//    private long workerCBranchISN;
//
//    @SerializedName("WorkerCISN")
//    private long workerCISN;
//
//    @SerializedName("CheckNumber")
//    private String checkNumber;
//
//    @SerializedName("CheckDueDate")
//    private String checkDueDate;
//
//    @SerializedName("CheckBankBranchISN")
//    private long checkBankBranchISN;
//
//    @SerializedName("CheckBankISN")
//    private long checkBankISN;
//
//    @SerializedName("CreateSource")
//    private int createSource;
//
//    @SerializedName("Latitude")
//    private float latitude;
//
//    @SerializedName("Longitude")
//    private float longitude;
//
//    @SerializedName("ShiftISN")
//    private Long shiftISN;
//
//    @SerializedName("WorkerName")
//    private String workerName;
//
//    @SerializedName("user_name")
//    private String userName;
//
//    @SerializedName("WorkStationName")
//    private String workStationName;
//
//    @SerializedName("WorkStation_ISN")
//    private String workStationISN;
//
//    @SerializedName("WorkStationBranchISN")
//    private String workStationBranchISN;
//
//
//    private GeneralRequestBody generalRequestBody;
//
//    public GeneralRequestBody getGeneralRequestBody() {
//        return generalRequestBody;
//    }
//
//    public void setGeneralRequestBody(GeneralRequestBody generalRequestBody) {
//        this.generalRequestBody = generalRequestBody;
//    }
//
//    public Long getShiftISN() {
//        return shiftISN;
//    }
//
//    public void setShiftISN(Long shiftISN) {
//        this.shiftISN = shiftISN;
//    }
//
//    public Integer getIllustrativeQuantity() {
//        return illustrativeQuantity;
//    }
//
//    public void setIllustrativeQuantity(Integer illustrativeQuantity) {
//        this.illustrativeQuantity = illustrativeQuantity;
//    }
//
//    public String getDeviceID() {
//        return deviceID;
//    }
//
//    public void setDeviceID(String deviceID) {
//        this.deviceID = deviceID;
//    }
//
//    public String getLoginCurrentWorkingDayDate() {
//        return loginCurrentWorkingDayDate;
//    }
//
//    public void setLoginCurrentWorkingDayDate(String loginCurrentWorkingDayDate) {
//        this.loginCurrentWorkingDayDate = loginCurrentWorkingDayDate;
//    }
//
//    public long getBranchISN() {
//        return branchISN;
//    }
//
//    public void setBranchISN(long branchISN) {
//        this.branchISN = branchISN;
//    }
//
//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }
//
//    public int getCashType() {
//        return cashType;
//    }
//
//    public void setCashType(int cashType) {
//        this.cashType = cashType;
//    }
//
//    public int getSaleType() {
//        return saleType;
//    }
//
//    public void setSaleType(int saleType) {
//        this.saleType = saleType;
//    }
//
//    public int getDealerType() {
//        return dealerType;
//    }
//
//    public void setDealerType(int dealerType) {
//        this.dealerType = dealerType;
//    }
//
//    public int getDealerBranchISN() {
//        return dealerBranchISN;
//    }
//
//    public void setDealerBranchISN(int dealerBranchISN) {
//        this.dealerBranchISN = dealerBranchISN;
//    }
//
//    public long getDealerISN() {
//        return dealerISN;
//    }
//
//    public void setDealerISN(long dealerISN) {
//        this.dealerISN = dealerISN;
//    }
//
//    public long getSalesManBranchISN() {
//        return salesManBranchISN;
//    }
//
//    public void setSalesManBranchISN(long salesManBranchISN) {
//        this.salesManBranchISN = salesManBranchISN;
//    }
//
//    public long getSalesManISN() {
//        return salesManISN;
//    }
//
//    public void setSalesManISN(long salesManISN) {
//        this.salesManISN = salesManISN;
//    }
//
//    public String getHeaderNotes() {
//        return headerNotes;
//    }
//
//    public void setHeaderNotes(String headerNotes) {
//        this.headerNotes = headerNotes;
//    }
//
//    public double getTotalLinesValue() {
//        return totalLinesValue;
//    }
//
//    public void setTotalLinesValue(double totalLinesValue) {
//        this.totalLinesValue = totalLinesValue;
//    }
//
//    public double getServiceValue() {
//        return serviceValue;
//    }
//
//    public void setServiceValue(double serviceValue) {
//        this.serviceValue = serviceValue;
//    }
//
//    public double getServicePer() {
//        return servicePer;
//    }
//
//    public void setServicePer(double servicePer) {
//        this.servicePer = servicePer;
//    }
//
//    public double getDeliveryValue() {
//        return deliveryValue;
//    }
//
//    public void setDeliveryValue(double deliveryValue) {
//        this.deliveryValue = deliveryValue;
//    }
//
//    public double getTotalValueAfterServices() {
//        return totalValueAfterServices;
//    }
//
//    public void setTotalValueAfterServices(double totalValueAfterServices) {
//        this.totalValueAfterServices = totalValueAfterServices;
//    }
//
//    public double getBasicDiscountVal() {
//        return basicDiscountVal;
//    }
//
//    public void setBasicDiscountVal(double basicDiscountVal) {
//        this.basicDiscountVal = basicDiscountVal;
//    }
//
//    public double getBasicDiscountPer() {
//        return basicDiscountPer;
//    }
//
//    public void setBasicDiscountPer(double basicDiscountPer) {
//        this.basicDiscountPer = basicDiscountPer;
//    }
//
//    public double getTotalValueAfterDisc() {
//        return totalValueAfterDisc;
//    }
//
//    public void setTotalValueAfterDisc(double totalValueAfterDisc) {
//        this.totalValueAfterDisc = totalValueAfterDisc;
//    }
//
//    public double getBasicTaxVal() {
//        return basicTaxVal;
//    }
//
//    public void setBasicTaxVal(double basicTaxVal) {
//        this.basicTaxVal = basicTaxVal;
//    }
//
//    public double getBasicTaxPer() {
//        return basicTaxPer;
//    }
//
//    public void setBasicTaxPer(double basicTaxPer) {
//        this.basicTaxPer = basicTaxPer;
//    }
//
//    public double getTotalValueAfterTax() {
//        return totalValueAfterTax;
//    }
//
//    public void setTotalValueAfterTax(double totalValueAfterTax) {
//        this.totalValueAfterTax = totalValueAfterTax;
//    }
//
//    public double getNetValue() {
//        return netValue;
//    }
//
//    public void setNetValue(double netValue) {
//        this.netValue = netValue;
//    }
//
//    public double getPaidValue() {
//        return paidValue;
//    }
//
//    public void setPaidValue(double paidValue) {
//        this.paidValue = paidValue;
//    }
//
//    public double getRemainValue() {
//        return remainValue;
//    }
//
//    public void setRemainValue(double remainValue) {
//        this.remainValue = remainValue;
//    }
//
//    public long getSafeDepositeBranchISN() {
//        return safeDepositeBranchISN;
//    }
//
//    public void setSafeDepositeBranchISN(long safeDepositeBranchISN) {
//        this.safeDepositeBranchISN = safeDepositeBranchISN;
//    }
//
//    public long getSafeDepositeISN() {
//        return safeDepositeISN;
//    }
//
//    public void setSafeDepositeISN(long safeDepositeISN) {
//        this.safeDepositeISN = safeDepositeISN;
//    }
//
//    public long getBankBranchISN() {
//        return bankBranchISN;
//    }
//
//    public void setBankBranchISN(long bankBranchISN) {
//        this.bankBranchISN = bankBranchISN;
//    }
//
//    public long getBankISN() {
//        return bankISN;
//    }
//
//    public void setBankISN(long bankISN) {
//        this.bankISN = bankISN;
//    }
//
//    public String getTableNumber() {
//        return tableNumber;
//    }
//
//    public void setTableNumber(String tableNumber) {
//        this.tableNumber = tableNumber;
//    }
//
//    public String getDeliveryPhone() {
//        return deliveryPhone;
//    }
//
//    public void setDeliveryPhone(String deliveryPhone) {
//        this.deliveryPhone = deliveryPhone;
//    }
//
//    public String getDeliveryAddress() {
//        return deliveryAddress;
//    }
//
//    public void setDeliveryAddress(String deliveryAddress) {
//        this.deliveryAddress = deliveryAddress;
//    }
//
//    public long getWorkerCBranchISN() {
//        return workerCBranchISN;
//    }
//
//    public void setWorkerCBranchISN(long workerCBranchISN) {
//        this.workerCBranchISN = workerCBranchISN;
//    }
//
//    public long getWorkerCISN() {
//        return workerCISN;
//    }
//
//    public void setWorkerCISN(long workerCISN) {
//        this.workerCISN = workerCISN;
//    }
//
//    public String getCheckNumber() {
//        return checkNumber;
//    }
//
//    public void setCheckNumber(String checkNumber) {
//        this.checkNumber = checkNumber;
//    }
//
//    public String getCheckDueDate() {
//        return checkDueDate;
//    }
//
//    public void setCheckDueDate(String checkDueDate) {
//        this.checkDueDate = checkDueDate;
//    }
//
//    public long getCheckBankBranchISN() {
//        return checkBankBranchISN;
//    }
//
//    public void setCheckBankBranchISN(long checkBankBranchISN) {
//        this.checkBankBranchISN = checkBankBranchISN;
//    }
//
//    public long getCheckBankISN() {
//        return checkBankISN;
//    }
//
//    public void setCheckBankISN(long checkBankISN) {
//        this.checkBankISN = checkBankISN;
//    }
//
//    public int getCreateSource() {
//        return createSource;
//    }
//
//    public void setCreateSource(int createSource) {
//        this.createSource = createSource;
//    }
//
//    public float getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(float latitude) {
//        this.latitude = latitude;
//    }
//
//    public float getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(float longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getWorkerName() {
//        return workerName;
//    }
//
//    public void setWorkerName(String workerName) {
//        this.workerName = workerName;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getWorkStationName() {
//        return workStationName;
//    }
//
//    public void setWorkStationName(String workStationName) {
//        this.workStationName = workStationName;
//    }
//
//    public String getWorkStationISN() {
//        return workStationISN;
//    }
//
//    public void setWorkStationISN(String workStationISN) {
//        this.workStationISN = workStationISN;
//    }
//
//    public String getWorkStationBranchISN() {
//        return workStationBranchISN;
//    }
//
//    public void setWorkStationBranchISN(String workStationBranchISN) {
//        this.workStationBranchISN = workStationBranchISN;
//    }
//
//    public int getSelectedFoundation() {
//        return selectedFoundation;
//    }
//
//    public void setSelectedFoundation(int selectedFoundation) {
//        this.selectedFoundation = selectedFoundation;
//    }
//
//    public String getMustChooseWorker() {
//        return mustChooseWorker;
//    }
//
//    public void setMustChooseWorker(String mustChooseWorker) {
//        this.mustChooseWorker = mustChooseWorker;
//    }
//
//    public Long getMainExpMenuISN() {
//        return mainExpMenuISN;
//    }
//
//    public void setMainExpMenuISN(Long mainExpMenuISN) {
//        this.mainExpMenuISN = mainExpMenuISN;
//    }
//
//    public Long getMainExpMenuBranchISN() {
//        return mainExpMenuBranchISN;
//    }
//
//    public void setMainExpMenuBranchISN(Long mainExpMenuBranchISN) {
//        this.mainExpMenuBranchISN = mainExpMenuBranchISN;
//    }
//
//    public String getMainExpMenuName() {
//        return mainExpMenuName;
//    }
//
//    public void setMainExpMenuName(String mainExpMenuName) {
//        this.mainExpMenuName = mainExpMenuName;
//    }
//
//    public Long getSubExpMenuISN() {
//        return subExpMenuISN;
//    }
//
//    public void setSubExpMenuISN(Long subExpMenuISN) {
//        this.subExpMenuISN = subExpMenuISN;
//    }
//
//    public Long getSubExpMenuBranchISN() {
//        return subExpMenuBranchISN;
//    }
//
//    public void setSubExpMenuBranchISN(Long subExpMenuBranchISN) {
//        this.subExpMenuBranchISN = subExpMenuBranchISN;
//    }
//
//    public String getSubExpMenuName() {
//        return subExpMenuName;
//    }
//
//    public void setSubExpMenuName(String subExpMenuName) {
//        this.subExpMenuName = subExpMenuName;
//    }
//
//    public Long getSelectedWorkerBranchISN() {
//        return selectedWorkerBranchISN;
//    }
//
//    public void setSelectedWorkerBranchISN(Long selectedWorkerBranchISN) {
//        this.selectedWorkerBranchISN = selectedWorkerBranchISN;
//    }
//
//    public Long getSelectedWorkerISN() {
//        return selectedWorkerISN;
//    }
//
//    public void setSelectedWorkerISN(Long selectedWorkerISN) {
//        this.selectedWorkerISN = selectedWorkerISN;
//    }
//}
