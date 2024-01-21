package com.dataflowstores.dataflow.pojo.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UserData implements Serializable {
    @SerializedName("token")
    private String token;
    @SerializedName("permission")
    private int permission;
    @SerializedName("BranchISN")
    private long branchISN;
    @SerializedName("branchName")
    private String branchName = "";
    @SerializedName("FoundationName")
    private String foundationName;
    @SerializedName("DeviceID")
    private String DeviceID;

    @SerializedName("Worker_ISN")
    private long workerISN;
    @SerializedName("WorkerBranchISN")
    private long WorkerBranchISN;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("WorkerName")
    private String workerName;
    @SerializedName("WorkStation_ISN")
    private long workStationISN;
    @SerializedName("WorkStationName")
    private String workStationName;
    @SerializedName("IsCheck")
    private int isCheck;
    @SerializedName("IsCasheir")
    private int isCasheir;
    @SerializedName("IsCredit")
    private int isCredit;
    @SerializedName("SafeDepositBranchISN")
    private long safeDepositBranchISN;
    @SerializedName("SafeDepositISN")
    private long safeDepositISN;
    @SerializedName("CashierStoreBranchISN")
    private long cashierStoreBranchISN;
    @SerializedName("CashierStoreISN")
    private long cashierStoreISN;
    @SerializedName("CashierSellPriceTypeBranchISN")
    private long cashierSellPriceTypeBranchISN;
    @SerializedName("CashierSellPriceType_ISN")
    private long cashierSellPriceTypeISN;

    @SerializedName("PricesTypeBranchISN")
    private long pricesTypeBranchISN;
    @SerializedName("PricesTypeISN")
    private long pricesTypeISN;
    @SerializedName("AllowSpecificDealersPrices")
    private int allowSpecificDealersPrices;
    @SerializedName("AllowStoreMinus")
    private int allowStoreMinus;
    @SerializedName("StockOutDefaultStoreBranchISN")
    private int StockOutDefaultStoreBranchISN;
    @SerializedName("StockOutDefaultStoreISN")
    private int StockOutDefaultStoreISN;
    @SerializedName("StockInDefaultStoreBranchISN")
    private int StockInDefaultStoreBranchISN;
    @SerializedName("StockInDefaultStoreISN")
    private int StockInDefaultStoreISN;
    @SerializedName("TransfereFromDefaultStoreBranchISN")
    private int TransfereFromDefaultStoreBranchISN;
    @SerializedName("TransfereFromDefaultStoreISN")
    private int TransfereFromDefaultStoreISN;
    @SerializedName("TransfereToDefaultStoreBranchISN")
    private int TransfereToDefaultStoreBranchISN;
    @SerializedName("TransfereToDefaultStoreISN")
    private int TransfereToDefaultStoreISN;

    @SerializedName("SalesDefaultStoreBranchISN")
    private int SalesDefaultStoreBranchISN;
    @SerializedName("ReSalesDefaultStoreBranchISN")
    private int ReSalesDefaultStoreBranchISN;
    @SerializedName("SalesDefaultStoreISN")
    private int SalesDefaultStoreISN;
    @SerializedName("ReSalesDefaultStoreISN")
    private int ReSalesDefaultStoreISN;
    @SerializedName("SupplyDefaultStoreBranchISN")
    private int SupplyDefaultStoreBranchISN;
    @SerializedName("ReSupplyDefaultStoreBranchISN")
    private int ReSupplyDefaultStoreBranchISN;
    @SerializedName("SupplyDefaultStoreISN")
    private int SupplyDefaultStoreISN;
    @SerializedName("ReSupplyDefaultStoreISN")
    private int ReSupplyDefaultStoreISN;
    @SerializedName("VendorID")
    private long vendorID;


    @SerializedName("FirstPeriodDefaultStoreBranchISN")
    private int FirstPeriodDefaultStoreBranchISN;
    @SerializedName("FirstPeriodDefaultStoreISN")
    private int FirstPeriodDefaultStoreISN;

    @SerializedName("LossesDefaultStoreBranchISN")
    private int LossesDefaultStoreBranchISN;
    @SerializedName("LossesDefaultStoreISN")
    private int LossesDefaultStoreISN;

    @SerializedName("ItemConfiguringDefaultStoreBranchISN")
    private int ItemConfiguringDefaultStoreBranchISN;
    @SerializedName("ItemConfiguringDefaultStoreISN")
    private int ItemConfiguringDefaultStoreISN;

    @SerializedName("InventoryModifyDefaultStoreBranchISN")
    private int InventoryModifyDefaultStoreBranchISN;
    @SerializedName("InventoryModifyDefaultStoreISN")
    private int InventoryModifyDefaultStoreISN;



    @SerializedName("MobileItemQuanModify")
    private int MobileItemQuanModify = 0;

    @SerializedName("MobileStockIn")
    private int MobileStockIn = 1;
    @SerializedName("MobileStockOut")
    private int MobileStockOut = 1;
    @SerializedName("MobileStoreTransfer")
    private int MobileStoreTransfer = 1;

    @SerializedName("MobileDealersBalanceEnquiry")
    private int MobileDealersBalanceEnquiry = 1;

    @SerializedName("MobileSuppliersBalanceEnquiry")
    private int MobileSuppliersBalanceEnquiry = 1;


    @SerializedName("MobileShowDealerCurrentBalanceInPrint")
    private int MobileShowDealerCurrentBalanceInPrint = 1;

    @SerializedName("MobileBonus")
    private int MobileBonus = 1;

    @SerializedName("MobileDiscount")
    private int MobileDiscount = 1;


    @SerializedName("MobileChangeSellPrice")
    private int MobileChangeSellPrice = 0;
    @SerializedName("MobileSales")
    private int MobileSales = 0;
    @SerializedName("MobileCashReceipts")
    private int MobileCashReceipts = 0;
    @SerializedName("MobileReSales")
    private int MobileReSales = 0;
    @SerializedName("MobileItemConfiguring")
    private int MobileItemConfiguring = 0;
    @SerializedName("MobileFirstPeriod")
    private int MobileFirstPeriod = 0;
    @SerializedName("MobileLosses")
    private int MobileLosses = 0;
    @SerializedName("MobileExpenses")
    private int MobileExpenses = 0;
    @SerializedName("MobileItemPricesEnquiry")
    private int MobileItemPricesEnquiry = 0;
    @SerializedName("MobileItemPricesEnquiryBuy")
    private int MobileItemPricesEnquiryBuy = 0;
    @SerializedName("MobileMoneyReport")
    private int MobileMoneyReport = 0;
    @SerializedName("MobileInventoryReport")
    private int MobileInventoryReport = 0;
    @SerializedName("AllBranchesWorker")
    private int AllBranchesWorker = 0;
    @SerializedName("MobileCashierMovesReport")
    private int MobileCashierMovesReport = 0;

    @SerializedName("MobileItemsSalesReport")
    private int MobileItemsSalesReport = 0;

    @SerializedName("MobilePreviousDatesInReports")
    private int MobilePreviousDatesInReports = 0;


    @SerializedName("LogIn_BISN")
    String LogIn_BISN;
    @SerializedName("LogIn_UID")
    String LogIn_UID;
    @SerializedName("LogIn_WBISN")
    String LogIn_WBISN;
    @SerializedName("LogIn_WISN")
    String LogIn_WISN;
    @SerializedName("LogIn_WName")
    String LogIn_WName;
    @SerializedName("LogIn_WSBISN")
    String LogIn_WSBISN;
    @SerializedName("LogIn_WSISN")
    String LogIn_WSISN;
    @SerializedName("LogIn_WSName")
    String LogIn_WSName;
    @SerializedName("LogIn_CS")
    String LogIn_CS;
    @SerializedName("LogIn_VN")
    String LogIn_VN;
    @SerializedName("LogIn_FAlternative")
    String LogIn_FAlternative;
    @SerializedName("InvoiceCurrentBalanceTimeInInvoice")
    String InvoiceCurrentBalanceTimeInInvoice;

    @SerializedName("MobileModifyPriceInSale")
    String MobileModifyPriceInSale;

    @SerializedName("MobileModifyPriceInReSale")
    String MobileModifyPriceInReSale;

    @SerializedName("MobileModifyPriceInSupply")
    String MobileModifyPriceInSupply;

    @SerializedName("MobileModifyPriceInReSupply")
    String MobileModifyPriceInReSupply;

    @SerializedName("MobileReportsSuppliersSearch")
    String MobileReportsSuppliersSearch;

    @SerializedName("MobileItemsSupplyReport")
    String MobileItemsSupplyReport;
    @SerializedName("MobileSupply")
    Integer MobileSupply;
    @SerializedName("MobileReSupply")
    Integer MobileReSupply;
    @SerializedName("MobileTax")
    Integer MobileTax;
    @SerializedName("MobilePayment")
    Integer MobilePayment;


    @SerializedName("MobileSalesMaxDiscPer")
    Integer MobileSalesMaxDiscPer;


    @SerializedName("ShiftSystemActivate")
    Integer ShiftSystemActivate;

    @SerializedName("LogIn_ShiftBranchISN")
    Integer LogIn_ShiftBranchISN;

    @SerializedName("LogIn_Spare1")
    Integer LogIn_Spare1;

    @SerializedName("LogIn_Spare2")
    Integer LogIn_Spare2;

    @SerializedName("LogIn_Spare3")
    Integer LogIn_Spare3;

    @SerializedName("LogIn_ShiftISN")
    Integer LogIn_ShiftISN;

    @SerializedName("LogIn_Spare4")
    Integer LogIn_Spare4;
    @SerializedName("LogIn_Spare5")
    Integer LogIn_Spare5;
    @SerializedName("LogIn_Spare6")
    Integer LogIn_Spare6;

    @SerializedName("MobileSearchMilliSecondsWait")
    Integer MobileSearchMilliSecondsWait;


    @SerializedName("MobileSearchTypeLetterCount")
    Integer MobileSearchTypeLetterCount;

    public Integer getMobileSearchMilliSecondsWait() {
        return MobileSearchMilliSecondsWait;
    }

    public Integer getMobileSearchTypeLetterCount() {
        return MobileSearchTypeLetterCount;
    }

    public Integer getShiftSystemActivate() {
        return ShiftSystemActivate;
    }

    public Integer getMobileSalesMaxDiscPer() {
        return MobileSalesMaxDiscPer;
    }

    public Integer getLogIn_ShiftBranchISN() {
        return LogIn_ShiftBranchISN;
    }

    public Integer getLogIn_Spare1() {
        return LogIn_Spare1;
    }

    public Integer getLogIn_Spare2() {
        return LogIn_Spare2;
    }

    public Integer getLogIn_Spare3() {
        return LogIn_Spare3;
    }

    public Integer getLogIn_ShiftISN() {
        return LogIn_ShiftISN;
    }

    public Integer getLogIn_Spare4() {
        return LogIn_Spare4;
    }

    public Integer getLogIn_Spare5() {
        return LogIn_Spare5;
    }

    public Integer getLogIn_Spare6() {
        return LogIn_Spare6;
    }

    //    ShiftSystemActivate
//            LogIn_ShiftBranchISN
//    LogIn_ShiftISN
//            LogIn_Spare1
//    LogIn_Spare2
//            LogIn_Spare3
//    LogIn_Spare4
//            LogIn_Spare5
//    LogIn_Spare6
    public Integer getMobilePayment() {
        return MobilePayment;
    }

    public Integer getMobileTax() {
        return MobileTax;
    }

    public Integer getMobileSupply() {
        return MobileSupply;
    }

    public Integer getMobileReSupply() {
        return MobileReSupply;
    }

    public int getSupplyDefaultStoreISN() {
        return SupplyDefaultStoreISN;
    }
    public int getReSupplyDefaultStoreISN() {
        return ReSupplyDefaultStoreISN;
    }
    public int getSupplyDefaultStoreBranchISN() {
        return SupplyDefaultStoreBranchISN;
    }
    public void setSupplyDefaultStoreBranchISN(int supplyDefaultStoreBranchISN) {
        SupplyDefaultStoreBranchISN = supplyDefaultStoreBranchISN;
    }

    public int getReSupplyDefaultStoreBranchISN() {
        return ReSupplyDefaultStoreBranchISN;
    }

    public void setReSupplyDefaultStoreBranchISN(int reSupplyDefaultStoreBranchISN) {
        ReSupplyDefaultStoreBranchISN = reSupplyDefaultStoreBranchISN;
    }

    public String getMobileItemsSupplyReport() {
        return MobileItemsSupplyReport;
    }

    public void setMobileItemsSupplyReport(String mobileItemsSupplyReport) {
        MobileItemsSupplyReport = mobileItemsSupplyReport;
    }

    public String getMobileReportsSuppliersSearch() {
        return MobileReportsSuppliersSearch;
    }

    public void setMobileReportsSuppliersSearch(String mobileReportsSuppliersSearch) {
        MobileReportsSuppliersSearch = mobileReportsSuppliersSearch;
    }

    public String getMobileModifyPriceInSale() {
        return MobileModifyPriceInSale;
    }

    public void setMobileModifyPriceInSale(String mobileModifyPriceInSale) {
        MobileModifyPriceInSale = mobileModifyPriceInSale;
    }

    public String getMobileModifyPriceInReSale() {
        return MobileModifyPriceInReSale;
    }

    public void setMobileModifyPriceInReSale(String mobileModifyPriceInReSale) {
        MobileModifyPriceInReSale = mobileModifyPriceInReSale;
    }

    public String getMobileModifyPriceInSupply() {
        return MobileModifyPriceInSupply;
    }

    public void setMobileModifyPriceInSupply(String mobileModifyPriceInSupply) {
        MobileModifyPriceInSupply = mobileModifyPriceInSupply;
    }

    public String getMobileModifyPriceInReSupply() {
        return MobileModifyPriceInReSupply;
    }

    public void setMobileModifyPriceInReSupply(String mobileModifyPriceInReSupply) {
        MobileModifyPriceInReSupply = mobileModifyPriceInReSupply;
    }

    public String getInvoiceCurrentBalanceTimeInInvoice() {
        return InvoiceCurrentBalanceTimeInInvoice;
    }

    public void setInvoiceCurrentBalanceTimeInInvoice(String invoiceCurrentBalanceTimeInInvoice) {
        InvoiceCurrentBalanceTimeInInvoice = invoiceCurrentBalanceTimeInInvoice;
    }

    public int getMobileSuppliersBalanceEnquiry() {
        return MobileSuppliersBalanceEnquiry;
    }

    public void setMobileSuppliersBalanceEnquiry(int mobileSuppliersBalanceEnquiry) {
        MobileSuppliersBalanceEnquiry = mobileSuppliersBalanceEnquiry;
    }

    public int getMobileItemPricesEnquiryBuy() {
        return MobileItemPricesEnquiryBuy;
    }

    public void setMobileItemPricesEnquiryBuy(int mobileItemPricesEnquiryBuy) {
        MobileItemPricesEnquiryBuy = mobileItemPricesEnquiryBuy;
    }

    public String getLogIn_BISN() {
        return LogIn_BISN;
    }

    public void setLogIn_BISN(String logIn_BISN) {
        LogIn_BISN = logIn_BISN;
    }

    public String getLogIn_UID() {
        return LogIn_UID;
    }

    public void setLogIn_UID(String logIn_UID) {
        LogIn_UID = logIn_UID;
    }

    public String getLogIn_WBISN() {
        return LogIn_WBISN;
    }

    public void setLogIn_WBISN(String logIn_WBISN) {
        LogIn_WBISN = logIn_WBISN;
    }

    public String getLogIn_WISN() {
        return LogIn_WISN;
    }

    public void setLogIn_WISN(String logIn_WISN) {
        LogIn_WISN = logIn_WISN;
    }

    public String getLogIn_WName() {
        return LogIn_WName;
    }

    public void setLogIn_WName(String logIn_WName) {
        LogIn_WName = logIn_WName;
    }

    public String getLogIn_WSBISN() {
        return LogIn_WSBISN;
    }

    public void setLogIn_WSBISN(String logIn_WSBISN) {
        LogIn_WSBISN = logIn_WSBISN;
    }

    public String getLogIn_WSISN() {
        return LogIn_WSISN;
    }

    public void setLogIn_WSISN(String logIn_WSISN) {
        LogIn_WSISN = logIn_WSISN;
    }

    public String getLogIn_WSName() {
        return LogIn_WSName;
    }

    public void setLogIn_WSName(String logIn_WSName) {
        LogIn_WSName = logIn_WSName;
    }

    public String getLogIn_CS() {
        return LogIn_CS;
    }

    public void setLogIn_CS(String logIn_CS) {
        LogIn_CS = logIn_CS;
    }

    public String getLogIn_VN() {
        return LogIn_VN;
    }

    public void setLogIn_VN(String logIn_VN) {
        LogIn_VN = logIn_VN;
    }

    public String getLogIn_FAlternative() {
        return LogIn_FAlternative;
    }

    public void setLogIn_FAlternative(String logIn_FAlternative) {
        LogIn_FAlternative = logIn_FAlternative;
    }

    public int getMobilePreviousDatesInReports() {
        return MobilePreviousDatesInReports;
    }

    public void setMobilePreviousDatesInReports(int mobilePreviousDatesInReports) {
        MobilePreviousDatesInReports = mobilePreviousDatesInReports;
    }

    public int getMobileItemsSalesReport() {
        return MobileItemsSalesReport;
    }

    public void setMobileItemsSalesReport(int mobileItemsSalesReport) {
        MobileItemsSalesReport = mobileItemsSalesReport;
    }

    public int getMobileCashierMovesReport() {
        return MobileCashierMovesReport;
    }

    public void setMobileCashierMovesReport(int mobileCashierMovesReport) {
        MobileCashierMovesReport = mobileCashierMovesReport;
    }

    public int getAllBranchesWorker() {
        return AllBranchesWorker;
    }


    public long getVendorID() {
        return vendorID;
    }

    public void setVendorID(long vendorID) {
        this.vendorID = vendorID;
    }

    public void setAllBranchesWorker(int allBranchesWorker) {
        AllBranchesWorker = allBranchesWorker;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public int getSalesDefaultStoreBranchISN() {
        return SalesDefaultStoreBranchISN;
    }

    public void setSalesDefaultStoreBranchISN(int salesDefaultStoreBranchISN) {
        SalesDefaultStoreBranchISN = salesDefaultStoreBranchISN;
    }

    public int getSalesDefaultStoreISN() {
        return SalesDefaultStoreISN;
    }

    public void setSalesDefaultStoreISN(int salesDefaultStoreISN) {
        SalesDefaultStoreISN = salesDefaultStoreISN;
    }

    public int getReSalesDefaultStoreBranchISN() {
        return ReSalesDefaultStoreBranchISN;
    }

    public void setReSalesDefaultStoreBranchISN(int reSalesDefaultStoreBranchISN) {
        ReSalesDefaultStoreBranchISN = reSalesDefaultStoreBranchISN;
    }

    public int getReSalesDefaultStoreISN() {
        return ReSalesDefaultStoreISN;
    }

    public void setReSalesDefaultStoreISN(int reSalesDefaultStoreISN) {
        ReSalesDefaultStoreISN = reSalesDefaultStoreISN;
    }

    public int getFirstPeriodDefaultStoreBranchISN() {
        return FirstPeriodDefaultStoreBranchISN;
    }

    public void setFirstPeriodDefaultStoreBranchISN(int firstPeriodDefaultStoreBranchISN) {
        FirstPeriodDefaultStoreBranchISN = firstPeriodDefaultStoreBranchISN;
    }

    public int getFirstPeriodDefaultStoreISN() {
        return FirstPeriodDefaultStoreISN;
    }

    public void setFirstPeriodDefaultStoreISN(int firstPeriodDefaultStoreISN) {
        FirstPeriodDefaultStoreISN = firstPeriodDefaultStoreISN;
    }

    public int getLossesDefaultStoreBranchISN() {
        return LossesDefaultStoreBranchISN;
    }

    public void setLossesDefaultStoreBranchISN(int lossesDefaultStoreBranchISN) {
        LossesDefaultStoreBranchISN = lossesDefaultStoreBranchISN;
    }

    public int getLossesDefaultStoreISN() {
        return LossesDefaultStoreISN;
    }

    public void setLossesDefaultStoreISN(int lossesDefaultStoreISN) {
        LossesDefaultStoreISN = lossesDefaultStoreISN;
    }

    public int getItemConfiguringDefaultStoreBranchISN() {
        return ItemConfiguringDefaultStoreBranchISN;
    }

    public void setItemConfiguringDefaultStoreBranchISN(int itemConfiguringDefaultStoreBranchISN) {
        ItemConfiguringDefaultStoreBranchISN = itemConfiguringDefaultStoreBranchISN;
    }

    public int getItemConfiguringDefaultStoreISN() {
        return ItemConfiguringDefaultStoreISN;
    }

    public void setItemConfiguringDefaultStoreISN(int itemConfiguringDefaultStoreISN) {
        ItemConfiguringDefaultStoreISN = itemConfiguringDefaultStoreISN;
    }

    public int getInventoryModifyDefaultStoreBranchISN() {
        return InventoryModifyDefaultStoreBranchISN;
    }

    public void setInventoryModifyDefaultStoreBranchISN(int inventoryModifyDefaultStoreBranchISN) {
        InventoryModifyDefaultStoreBranchISN = inventoryModifyDefaultStoreBranchISN;
    }

    public int getInventoryModifyDefaultStoreISN() {
        return InventoryModifyDefaultStoreISN;
    }

    public void setInventoryModifyDefaultStoreISN(int inventoryModifyDefaultStoreISN) {
        InventoryModifyDefaultStoreISN = inventoryModifyDefaultStoreISN;
    }

    public int getMobileItemQuanModify() {
        return MobileItemQuanModify;
    }

    public void setMobileItemQuanModify(int mobileItemQuanModify) {
        MobileItemQuanModify = mobileItemQuanModify;
    }

    public int getMobileMoneyReport() {
        return MobileMoneyReport;
    }

    public void setMobileMoneyReport(int mobileMoneyReport) {
        MobileMoneyReport = mobileMoneyReport;
    }

    public int getMobileInventoryReport() {
        return MobileInventoryReport;
    }

    public void setMobileInventoryReport(int mobileInventoryReport) {
        MobileInventoryReport = mobileInventoryReport;
    }

    public int getMobileItemPricesEnquiry() {
        return MobileItemPricesEnquiry;
    }

    public void setMobileItemPricesEnquiry(int mobileItemPricesEnquiry) {
        MobileItemPricesEnquiry = mobileItemPricesEnquiry;
    }

    public int getMobileExpenses() {
        return MobileExpenses;
    }

    public void setMobileExpenses(int mobileExpenses) {
        MobileExpenses = mobileExpenses;
    }

    public int getMobileReSales() {
        return MobileReSales;
    }

    public void setMobileReSales(int mobileReSales) {
        MobileReSales = mobileReSales;
    }

    public int getMobileItemConfiguring() {
        return MobileItemConfiguring;
    }

    public void setMobileItemConfiguring(int mobileItemConfiguring) {
        MobileItemConfiguring = mobileItemConfiguring;
    }

    public int getMobileFirstPeriod() {
        return MobileFirstPeriod;
    }

    public void setMobileFirstPeriod(int mobileFirstPeriod) {
        MobileFirstPeriod = mobileFirstPeriod;
    }

    public int getMobileLosses() {
        return MobileLosses;
    }

    public void setMobileLosses(int mobileLosses) {
        MobileLosses = mobileLosses;
    }

    public int getMobileSales() {
        return MobileSales;
    }

    public void setMobileSales(int mobileSales) {
        MobileSales = mobileSales;
    }

    public int getMobileCashReceipts() {
        return MobileCashReceipts;
    }

    public void setMobileCashReceipts(int mobileCashReceipts) {
        MobileCashReceipts = mobileCashReceipts;
    }

    public int getMobileChangeSellPrice() {
        return MobileChangeSellPrice;
    }

    public void setMobileChangeSellPrice(int mobileChangeSellPrice) {
        MobileChangeSellPrice = mobileChangeSellPrice;
    }

    public int getMobileBonus() {
        return MobileBonus;
    }

    public void setMobileBonus(int mobileBonus) {
        MobileBonus = mobileBonus;
    }

    public int getMobileDiscount() {
        return MobileDiscount;
    }

    public void setMobileDiscount(int mobileDiscount) {
        MobileDiscount = mobileDiscount;
    }

    public int getMobileDealersBalanceEnquiry() {
        return MobileDealersBalanceEnquiry;
    }

    public void setMobileDealersBalanceEnquiry(int mobileDealersBalanceEnquiry) {
        MobileDealersBalanceEnquiry = mobileDealersBalanceEnquiry;
    }

    public int getMobileShowDealerCurrentBalanceInPrint() {
        return MobileShowDealerCurrentBalanceInPrint;
    }

    public void setMobileShowDealerCurrentBalanceInPrint(int mobileShowDealerCurrentBalanceInPrint) {
        MobileShowDealerCurrentBalanceInPrint = mobileShowDealerCurrentBalanceInPrint;
    }

    public int getMobileStockIn() {
        return MobileStockIn;
    }

    public void setMobileStockIn(int mobileStockIn) {
        MobileStockIn = mobileStockIn;
    }

    public int getMobileStockOut() {
        return MobileStockOut;
    }

    public void setMobileStockOut(int mobileStockOut) {
        MobileStockOut = mobileStockOut;
    }

    public int getMobileStoreTransfer() {
        return MobileStoreTransfer;
    }

    public void setMobileStoreTransfer(int mobileStoreTransfer) {
        MobileStoreTransfer = mobileStoreTransfer;
    }

    public int getStockOutDefaultStoreBranchISN() {
        return StockOutDefaultStoreBranchISN;
    }

    public void setStockOutDefaultStoreBranchISN(int stockOutDefaultStoreBranchISN) {
        StockOutDefaultStoreBranchISN = stockOutDefaultStoreBranchISN;
    }

    public int getStockOutDefaultStoreISN() {
        return StockOutDefaultStoreISN;
    }

    public void setStockOutDefaultStoreISN(int stockOutDefaultStoreISN) {
        StockOutDefaultStoreISN = stockOutDefaultStoreISN;
    }

    public int getStockInDefaultStoreBranchISN() {
        return StockInDefaultStoreBranchISN;
    }

    public void setStockInDefaultStoreBranchISN(int stockInDefaultStoreBranchISN) {
        StockInDefaultStoreBranchISN = stockInDefaultStoreBranchISN;
    }

    public int getStockInDefaultStoreISN() {
        return StockInDefaultStoreISN;
    }

    public void setStockInDefaultStoreISN(int stockInDefaultStoreISN) {
        StockInDefaultStoreISN = stockInDefaultStoreISN;
    }

    public int getTransfereFromDefaultStoreBranchISN() {
        return TransfereFromDefaultStoreBranchISN;
    }

    public void setTransfereFromDefaultStoreBranchISN(int transfereFromDefaultStoreBranchISN) {
        TransfereFromDefaultStoreBranchISN = transfereFromDefaultStoreBranchISN;
    }

    public int getTransfereFromDefaultStoreISN() {
        return TransfereFromDefaultStoreISN;
    }

    public void setTransfereFromDefaultStoreISN(int transfereFromDefaultStoreISN) {
        TransfereFromDefaultStoreISN = transfereFromDefaultStoreISN;
    }

    public int getTransfereToDefaultStoreBranchISN() {
        return TransfereToDefaultStoreBranchISN;
    }

    public void setTransfereToDefaultStoreBranchISN(int transfereToDefaultStoreBranchISN) {
        TransfereToDefaultStoreBranchISN = transfereToDefaultStoreBranchISN;
    }

    public int getTransfereToDefaultStoreISN() {
        return TransfereToDefaultStoreISN;
    }

    public void setTransfereToDefaultStoreISN(int transfereToDefaultStoreISN) {
        TransfereToDefaultStoreISN = transfereToDefaultStoreISN;
    }

    public int getAllowStoreMinus() {
        return allowStoreMinus;
    }

    public void setAllowStoreMinus(int allowStoreMinus) {
        this.allowStoreMinus = allowStoreMinus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public long getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(long branchISN) {
        this.branchISN = branchISN;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getFoundationName() {
        return foundationName;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public long getWorkerISN() {
        return workerISN;
    }

    public void setWorkerISN(long workerISN) {
        this.workerISN = workerISN;
    }

    public long getWorkerBranchISN() {
        return WorkerBranchISN;
    }

    public void setWorkerBranchISN(long workerBranchISN) {
        WorkerBranchISN = workerBranchISN;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public long getWorkStationISN() {
        return workStationISN;
    }

    public void setWorkStationISN(long workStationISN) {
        this.workStationISN = workStationISN;
    }

    public String getWorkStationName() {
        return workStationName;
    }

    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsCasheir() {
        return isCasheir;
    }

    public void setIsCasheir(int isCasheir) {
        this.isCasheir = isCasheir;
    }

    public int getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(int isCredit) {
        this.isCredit = isCredit;
    }

    public long getSafeDepositBranchISN() {
        return safeDepositBranchISN;
    }

    public void setSafeDepositBranchISN(long safeDepositBranchISN) {
        this.safeDepositBranchISN = safeDepositBranchISN;
    }

    public long getSafeDepositISN() {
        return safeDepositISN;
    }

    public void setSafeDepositISN(long safeDepositISN) {
        this.safeDepositISN = safeDepositISN;
    }

    public long getCashierStoreBranchISN() {
        return cashierStoreBranchISN;
    }

    public void setCashierStoreBranchISN(long cashierStoreBranchISN) {
        this.cashierStoreBranchISN = cashierStoreBranchISN;
    }

    public long getCashierStoreISN() {
        return cashierStoreISN;
    }

    public void setCashierStoreISN(long cashierStoreISN) {
        this.cashierStoreISN = cashierStoreISN;
    }

    public long getCashierSellPriceTypeBranchISN() {
        return cashierSellPriceTypeBranchISN;
    }

    public void setCashierSellPriceTypeBranchISN(long cashierSellPriceTypeBranchISN) {
        this.cashierSellPriceTypeBranchISN = cashierSellPriceTypeBranchISN;
    }

    public long getCashierSellPriceTypeISN() {
        return cashierSellPriceTypeISN;
    }

    public void setCashierSellPriceTypeISN(long cashierSellPriceTypeISN) {
        this.cashierSellPriceTypeISN = cashierSellPriceTypeISN;
    }

    public long getPricesTypeBranchISN() {
        return pricesTypeBranchISN;
    }

    public void setPricesTypeBranchISN(long pricesTypeBranchISN) {
        this.pricesTypeBranchISN = pricesTypeBranchISN;
    }

    public long getPricesTypeISN() {
        return pricesTypeISN;
    }

    public void setPricesTypeISN(long pricesTypeISN) {
        this.pricesTypeISN = pricesTypeISN;
    }

    public int getAllowSpecificDealersPrices() {
        return allowSpecificDealersPrices;
    }

    public void setAllowSpecificDealersPrices(int allowSpecificDealersPrices) {
        this.allowSpecificDealersPrices = allowSpecificDealersPrices;
    }
}
