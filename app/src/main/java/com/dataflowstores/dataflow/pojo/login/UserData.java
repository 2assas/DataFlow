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
    @SerializedName("SalesDefaultStoreISN")
    private int SalesDefaultStoreISN;


    @SerializedName("ReSalesDefaultStoreBranchISN")
    private int ReSalesDefaultStoreBranchISN;
    @SerializedName("ReSalesDefaultStoreISN")
    private int ReSalesDefaultStoreISN;


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
    @SerializedName("MobileMoneyReport")
    private int MobileMoneyReport = 0;
    @SerializedName("MobileInventoryReport")
    private int MobileInventoryReport = 0;
    @SerializedName("AllBranchesWorker")
    private int AllBranchesWorker = 0;

    public int getAllBranchesWorker() {
        return AllBranchesWorker;
    }

    public void setAllBranchesWorker(int allBranchesWorker) {
        AllBranchesWorker = allBranchesWorker;
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
