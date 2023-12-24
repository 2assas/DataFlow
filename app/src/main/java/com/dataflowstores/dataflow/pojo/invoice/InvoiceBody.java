package com.dataflowstores.dataflow.pojo.invoice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class InvoiceBody implements Serializable {
    @SerializedName("BranchISN")
    Long BranchISN;
    @SerializedName("uiid")
    String uuid;
    @SerializedName("CashType")
    Integer CashType;
    @SerializedName("SaleType")
    Integer SaleType;
    @SerializedName("DealerType")
    Integer DealerType;
    @SerializedName("DealerBranchISN")
    Integer DealerBranchISN;
    @SerializedName("DealerISN")
    Long DealerISN;
    @SerializedName("SalesManBranchISN")
    Long SalesManBranchISN;
    @SerializedName("SalesManISN")
    Long SalesManISN;
    @SerializedName("HeaderNotes")
    String HeaderNotes;
    @SerializedName("TotalLinesValue")
    Double TotalLinesValue;
    @SerializedName("ServiceValue")
    Double ServiceValue;
    @SerializedName("ServicePer")
    Double ServicePer;
    @SerializedName("DeliveryValue")
    Double DeliveryValue;
    @SerializedName("TotalValueAfterServices")
    Double TotalValueAfterServices;
    @SerializedName("BasicDiscountVal")
    Double BasicDiscountVal;
    @SerializedName("BasicDiscountPer")
    Double BasicDiscountPer;
    @SerializedName("TotalValueAfterDisc")
    Double TotalValueAfterDisc;
    @SerializedName("BasicTaxVal")
    Double BasicTaxVal;
    @SerializedName("BasicTaxPer")
    Double BasicTaxPer;
    @SerializedName("TotalValueAfterTax")
    Double TotalValueAfterTax;
    @SerializedName("NetValue")
    Double NetValue;
    @SerializedName("PaidValue")
    Double PaidValue;
    @SerializedName("RemainValue")
    Double RemainValue;
    @SerializedName("SafeDepositeBranchISN")
    Long SafeDepositeBranchISN;
    @SerializedName("SafeDepositeISN")
    Long SafeDepositeISN;
    @SerializedName("BankBranchISN")
    Long BankBranchISN;
    @SerializedName("BankISN")
    Long BankISN;
    @SerializedName("TableNumber")
    String TableNumber;
    @SerializedName("DeliveryPhone")
    String DeliveryPhone;
    @SerializedName("DeliveryAddress")
    String DeliveryAddress;
    @SerializedName("WorkerCBranchISN")
    Long WorkerCBranchISN;
    @SerializedName("WorkerCISN")
    Long WorkerCISN;
    @SerializedName("CheckNumber")
    String CheckNumber;
    @SerializedName("CheckDueDate")
    String CheckDueDate;//HERE
    @SerializedName("CheckBankBranchISN")
    Long CheckBankBranchISN;
    @SerializedName("CheckBankISN")
    Long CheckBankISN;
    @SerializedName("ItemBranchISN")
    ArrayList<Long> ItemBranchISN;
    @SerializedName("ItemISN")
    ArrayList<Long> ItemISN;
    @SerializedName("PriceTypeBranchISN")
    ArrayList<Long> PriceTypeBranchISN;
    @SerializedName("PriceTypeISN")
    ArrayList<Long> PriceTypeISN;
    @SerializedName("StoreBranchISN")
    ArrayList<Long> StoreBranchISN;
    @SerializedName("StoreISN")
    ArrayList<Long> StoreISN;
    @SerializedName("BasicQuantity")
    ArrayList<Float> BasicQuantity;
    @SerializedName("BonusQuantity")
    ArrayList<Float> BonusQuantity;
    @SerializedName("TotalQuantity")
    ArrayList<Float> TotalQuantity;
    @SerializedName("Price")
    ArrayList<Double> Price;
    @SerializedName("MeasureUnitBranchISN")
    ArrayList<Long> MeasureUnitBranchISN;
    @SerializedName("MeasureUnitISN")
    ArrayList<Long> MeasureUnitISN;
    @SerializedName("BasicMeasureUnitBranchISN")
    ArrayList<Long> BasicMeasureUnitBranchISN;
    @SerializedName("BasicMeasureUnitISN")
    ArrayList<Long> BasicMeasureUnitISN;
    @SerializedName("ItemSerial")
    ArrayList<String> ItemSerial;
    @SerializedName("itemExpireDate")
    ArrayList<String> ExpireDateItem;
    @SerializedName("ColorBranchISN")
    ArrayList<Long> ColorBranchISN;
    @SerializedName("ColorISN")
    ArrayList<Long> ColorISN;
    @SerializedName("SizeBranchISN")
    ArrayList<Long> SizeBranchISN;
    @SerializedName("SizeISN")
    ArrayList<Long> SizeISN;
    @SerializedName("SeasonBranchISN")
    ArrayList<Long> SeasonBranchISN;
    @SerializedName("SeasonISN")
    ArrayList<Long> SeasonISN;
    @SerializedName("Group1BranchISN")
    ArrayList<Long> Group1BranchISN;
    @SerializedName("Group1ISN")
    ArrayList<Long> Group1ISN;
    @SerializedName("Group2BranchISN")
    ArrayList<Long> Group2BranchISN;
    @SerializedName("Group2ISN")
    ArrayList<Long> Group2ISN;
    @SerializedName("LineNotes")
    ArrayList<String> LineNotes;
    @SerializedName("numberOFItems")
    Long numberOFItems;
    @SerializedName("CreateSource")
    Integer createSource;
    @SerializedName("NetPrice")
    ArrayList<Double> NetPrice;
    @SerializedName("BasicMeasureUnitQuantity")
    ArrayList<Double> BasicMeasureUnitQuantity;
    @SerializedName("ExpireDate")
    ArrayList<Boolean> ExpireDate;
    @SerializedName("Colors")
    ArrayList<Boolean> Colors;
    @SerializedName("Seasons")
    ArrayList<Boolean> Seasons;
    @SerializedName("Sizes")
    ArrayList<Boolean> Sizes;
    @SerializedName("Serial")
    ArrayList<Boolean> Serial;
    @SerializedName("Group1")
    ArrayList<Boolean> Group1;
    @SerializedName("Group2")
    ArrayList<Boolean> Group2;
    @SerializedName("ServiceItem")
    ArrayList<Boolean> ServiceItem;
    @SerializedName("ItemTax")
    ArrayList<Double> ItemTax;
    @SerializedName("TaxValue")
    ArrayList<Double> TaxValue;
    @SerializedName("TotalLinesTaxVal")
    Double TotalLinesTaxVal;
    @SerializedName("AllowStoreMinus")
    Integer allowStoreMinus;
    @SerializedName("ItemName")
    ArrayList<String> itemName;
    @SerializedName("Discount1")
    ArrayList<Double> discount1;
    @SerializedName("AllowStoreMinusConfirm")
    Integer AllowStoreMinusConfirm;
    @SerializedName("Latitude")
    float latitude;
    @SerializedName("Longitude")
    float Longitude;
    @SerializedName("invoiceType")
    String invoiceType;
    @SerializedName("MoveType")
    Integer moveType;
    @SerializedName("StoreBranchISN2")
    ArrayList<Long> StoreBranchISN2;
    @SerializedName("StoreISN2")
    ArrayList<Long> StoreISN2;

    @SerializedName("WorkerName")
    String WorkerName;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("WorkStationName")
    String WorkStationName;
    @SerializedName("WorkStation_ISN")
    String WorkStation_ISN;
    @SerializedName("WorkStationBranchISN")
    String WorkStationBranchISN;
    @SerializedName("BranchISNStockMove")
    String BranchISNStockMove;

    @SerializedName("SelectedFoundation")
    int selectedFoundation;
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

    @SerializedName("AllowCurrentStoreMinus")
    ArrayList<Integer> AllowCurrentStoreMinus;

    @SerializedName("ProductStoreName")
    ArrayList<String> ProductStoreName;


    public InvoiceBody(Long branchISN, String uuid, Integer cashType, Integer saleType, Integer dealerType, Integer dealerBranchISN, Long dealerISN, Long salesManBranchISN, Long salesManISN, String headerNotes, Double totalLinesValue, Double serviceValue, Double servicePer, Double deliveryValue, Double totalValueAfterServices, Double basicDiscountVal, Double basicDiscountPer, Double totalValueAfterDisc, Double basicTaxVal, Double basicTaxPer, Double totalValueAfterTax, Double netValue, Double paidValue, Double remainValue, Long safeDepositeBranchISN, Long safeDepositeISN, Long bankBranchISN, Long bankISN, String tableNumber, String deliveryPhone, String deliveryAddress, Long workerCBranchISN, Long workerCISN, String checkNumber, String checkDueDate, Long checkBankBranchISN, Long checkBankISN, ArrayList<Long> itemBranchISN, ArrayList<Long> itemISN, ArrayList<Long> priceTypeBranchISN, ArrayList<Long> priceTypeISN, ArrayList<Long> storeBranchISN, ArrayList<Long> storeISN, ArrayList<Float> basicQuantity, ArrayList<Float> bonusQuantity, ArrayList<Float> totalQuantity, ArrayList<Double> price, ArrayList<Long> measureUnitBranchISN, ArrayList<Long> measureUnitISN, ArrayList<Long> basicMeasureUnitBranchISN, ArrayList<Long> basicMeasureUnitISN, ArrayList<String> itemSerial, ArrayList<String> expireDateItem, ArrayList<Long> colorBranchISN, ArrayList<Long> colorISN, ArrayList<Long> sizeBranchISN, ArrayList<Long> sizeISN, ArrayList<Long> seasonBranchISN, ArrayList<Long> seasonISN, ArrayList<Long> group1BranchISN, ArrayList<Long> group1ISN, ArrayList<Long> group2BranchISN, ArrayList<Long> group2ISN, ArrayList<String> lineNotes, Long numberOFItems, Integer createSource, ArrayList<Double> netPrice, ArrayList<Double> basicMeasureUnitQuantity, ArrayList<Boolean> expireDate, ArrayList<Boolean> colors, ArrayList<Boolean> seasons, ArrayList<Boolean> sizes, ArrayList<Boolean> serial, ArrayList<Boolean> group1, ArrayList<Boolean> group2, ArrayList<Boolean> serviceItem, ArrayList<Double> itemTax, ArrayList<Double> taxValue, Double totalLinesTaxVal, Integer allowStoreMinus, ArrayList<String> itemName, ArrayList<Double> discount1, Integer allowStoreMinusConfirm, float latitude, float longitude, String invoiceType, Integer moveType, ArrayList<Long> storeBranchISN2, ArrayList<Long> storeISN2, String workerName, String user_name, String workStationName, String workStation_ISN, String workStationBranchISN, String BranchISNStockMove, int SelectedFoundation, String logIn_BISN, String logIn_UID, String logIn_WBISN, String logIn_WISN, String logIn_WName, String logIn_WSBISN, String logIn_WSISN, String logIn_WSName, String logIn_CS, String logIn_VN, String logIn_FAlternative, ArrayList<Integer> productAllowStoreMinus, ArrayList<String> productStoreName) {
        BranchISN = branchISN;
        this.uuid = uuid;
        CashType = cashType;
        SaleType = saleType;
        DealerType = dealerType;
        DealerBranchISN = dealerBranchISN;
        DealerISN = dealerISN;
        SalesManBranchISN = salesManBranchISN;
        SalesManISN = salesManISN;
        HeaderNotes = headerNotes;
        TotalLinesValue = totalLinesValue;
        ServiceValue = serviceValue;
        ServicePer = servicePer;
        DeliveryValue = deliveryValue;
        TotalValueAfterServices = totalValueAfterServices;
        BasicDiscountVal = basicDiscountVal;
        BasicDiscountPer = basicDiscountPer;
        TotalValueAfterDisc = totalValueAfterDisc;
        BasicTaxVal = basicTaxVal;
        BasicTaxPer = basicTaxPer;
        TotalValueAfterTax = totalValueAfterTax;
        NetValue = netValue;
        PaidValue = paidValue;
        RemainValue = remainValue;
        SafeDepositeBranchISN = safeDepositeBranchISN;
        SafeDepositeISN = safeDepositeISN;
        BankBranchISN = bankBranchISN;
        BankISN = bankISN;
        TableNumber = tableNumber;
        DeliveryPhone = deliveryPhone;
        DeliveryAddress = deliveryAddress;
        WorkerCBranchISN = workerCBranchISN;
        WorkerCISN = workerCISN;
        CheckNumber = checkNumber;
        CheckDueDate = checkDueDate;
        CheckBankBranchISN = checkBankBranchISN;
        CheckBankISN = checkBankISN;
        ItemBranchISN = itemBranchISN;
        ItemISN = itemISN;
        PriceTypeBranchISN = priceTypeBranchISN;
        PriceTypeISN = priceTypeISN;
        StoreBranchISN = storeBranchISN;
        StoreISN = storeISN;
        BasicQuantity = basicQuantity;
        BonusQuantity = bonusQuantity;
        TotalQuantity = totalQuantity;
        Price = price;
        MeasureUnitBranchISN = measureUnitBranchISN;
        MeasureUnitISN = measureUnitISN;
        BasicMeasureUnitBranchISN = basicMeasureUnitBranchISN;
        BasicMeasureUnitISN = basicMeasureUnitISN;
        ItemSerial = itemSerial;
        ExpireDateItem = expireDateItem;
        ColorBranchISN = colorBranchISN;
        ColorISN = colorISN;
        SizeBranchISN = sizeBranchISN;
        SizeISN = sizeISN;
        SeasonBranchISN = seasonBranchISN;
        SeasonISN = seasonISN;
        Group1BranchISN = group1BranchISN;
        Group1ISN = group1ISN;
        Group2BranchISN = group2BranchISN;
        Group2ISN = group2ISN;
        LineNotes = lineNotes;
        this.numberOFItems = numberOFItems;
        this.createSource = createSource;
        NetPrice = netPrice;
        BasicMeasureUnitQuantity = basicMeasureUnitQuantity;
        ExpireDate = expireDate;
        Colors = colors;
        Seasons = seasons;
        Sizes = sizes;
        Serial = serial;
        Group1 = group1;
        Group2 = group2;
        ServiceItem = serviceItem;
        ItemTax = itemTax;
        TaxValue = taxValue;
        TotalLinesTaxVal = totalLinesTaxVal;
        this.allowStoreMinus = allowStoreMinus;
        this.itemName = itemName;
        this.discount1 = discount1;
        AllowStoreMinusConfirm = allowStoreMinusConfirm;
        this.latitude = latitude;
        Longitude = longitude;
        this.invoiceType = invoiceType;
        this.moveType = moveType;
        StoreBranchISN2 = storeBranchISN2;
        StoreISN2 = storeISN2;
        WorkerName = workerName;
        this.user_name = user_name;
        WorkStationName = workStationName;
        WorkStation_ISN = workStation_ISN;
        WorkStationBranchISN = workStationBranchISN;
        this.BranchISNStockMove = BranchISNStockMove;
        this.selectedFoundation = SelectedFoundation;
        LogIn_BISN = logIn_BISN;
        LogIn_UID = logIn_UID;
        LogIn_WBISN = logIn_WBISN;
        LogIn_WISN = logIn_WISN;
        LogIn_WName = logIn_WName;
        LogIn_WSBISN = logIn_WSBISN;
        LogIn_WSISN = logIn_WSISN;
        LogIn_WSName = logIn_WSName;
        LogIn_CS = logIn_CS;
        LogIn_VN = logIn_VN;
        LogIn_FAlternative = logIn_FAlternative;
        AllowCurrentStoreMinus = productAllowStoreMinus;
        ProductStoreName =productStoreName;
    }
}
