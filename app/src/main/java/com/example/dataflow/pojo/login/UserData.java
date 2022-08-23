package com.example.dataflow.pojo.login;

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
    private String branchName="";
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
    @SerializedName("WorkStationISN")
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

    @SerializedName("MobileStockIn")
    private int MobileStockIn=1;
    @SerializedName("MobileStockOut")
    private int MobileStockOut=1;
    @SerializedName("MobileStoreTransfer")
    private int MobileStoreTransfer=1;

    @SerializedName("MobileDealersBalanceEnquiry")
    private int MobileDealersBalanceEnquiry=1;


    @SerializedName("MobileShowDealerCurrentBalanceInPrint")
    private int MobileShowDealerCurrentBalanceInPrint =1;

    @SerializedName("MobileBonus")
    private int MobileBonus =1;

    @SerializedName("MobileDiscount")
    private int MobileDiscount =1;


    @SerializedName("MobileChangeSellPrice")
    private int MobileChangeSellPrice =0;


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
