package com.dataflowstores.dataflow.pojo.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerData implements Serializable {
    @SerializedName("DealerISNBranch")
    private int DealerISNBranch;
    @SerializedName("DealerName")
    private String DealerName;
    @SerializedName("Dealer_ISN")
    private long Dealer_ISN;
    @SerializedName("BranchISN")
    private int BranchISN;
    @SerializedName("DealerType")
    private int dealerType;
    @SerializedName("Dealer_Phone")
    private String dealerPhone;
    @SerializedName("Dealer_Address")
    private String dealerAddress;

    @SerializedName("DealerSellPriceTypeBranchISN")
    private String DealerSellPriceTypeBranchISN;

    @SerializedName("DealerSellPriceTypeISN")
    private String DealerSellPriceTypeISN;
    @SerializedName("DealerInvoiceDefaultDisc")
    private String DealerInvoiceDefaultDisc;
    @SerializedName("AllowForwardSell")
    private String AllowForwardSell;
    @SerializedName("SaleMaxCredit")
    private String SaleMaxCredit;
    @SerializedName("SaleLastAllowedDate")
    private String SaleLastAllowedDate;
    @SerializedName("SaleRenewMonthCount")
    private String SaleRenewMonthCount;

    public String getAllowForwardSell() {
        return AllowForwardSell;
    }

    public String getSaleLastAllowedDate() {
        return SaleLastAllowedDate;
    }

    public String getSaleMaxCredit() {
        return SaleMaxCredit;
    }

    public String getSaleRenewMonthCount() {
        return SaleRenewMonthCount;
    }

    public String getDealerInvoiceDefaultDisc() {
        return DealerInvoiceDefaultDisc;
    }
    public void setDealerInvoiceDefaultDisc(String dealerInvoiceDefaultDisc) {
        DealerInvoiceDefaultDisc = dealerInvoiceDefaultDisc;
    }

    public String getDealerSellPriceTypeBranchISN() {
        return DealerSellPriceTypeBranchISN;
    }

    public void setDealerSellPriceTypeBranchISN(String dealerSellPriceTypeBranchISN) {
        DealerSellPriceTypeBranchISN = dealerSellPriceTypeBranchISN;
    }

    public String getDealerSellPriceTypeISN() {
        return DealerSellPriceTypeISN;
    }

    public void setDealerSellPriceTypeISN(String dealerSellPriceTypeISN) {
        DealerSellPriceTypeISN = dealerSellPriceTypeISN;
    }

    public long getDealer_ISN() {
        return Dealer_ISN;
    }

    public void setDealer_ISN(long dealer_ISN) {
        Dealer_ISN = dealer_ISN;
    }

    public int getDealerISNBranch() {
        return DealerISNBranch;
    }

    public void setDealerISNBranch(int dealerISNBranch) {
        DealerISNBranch = dealerISNBranch;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }

    public int getDealerType() {
        return dealerType;
    }

    public void setDealerType(int dealerType) {
        this.dealerType = dealerType;
    }

    public String getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String dealerPhone) {
        this.dealerPhone = dealerPhone;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }
}
