package com.example.dataflow.webService;

import com.example.dataflow.pojo.financialReport.FinancialReportResponse;
import com.example.dataflow.pojo.financialReport.ReportBody;
import com.example.dataflow.pojo.invoice.Invoice;
import com.example.dataflow.pojo.invoice.InvoiceResponse;
import com.example.dataflow.pojo.login.LoginStatus;
import com.example.dataflow.pojo.product.Product;
import com.example.dataflow.pojo.receipts.ReceiptModel;
import com.example.dataflow.pojo.receipts.ReceiptResponse;
import com.example.dataflow.pojo.report.Branches;
import com.example.dataflow.pojo.report.StoreReportModel;
import com.example.dataflow.pojo.report.WorkersResponse;
import com.example.dataflow.pojo.settings.Banks;
import com.example.dataflow.pojo.settings.PriceType;
import com.example.dataflow.pojo.settings.SafeDeposit;
import com.example.dataflow.pojo.settings.Stores;
import com.example.dataflow.pojo.users.Customer;
import com.example.dataflow.pojo.users.CustomerBalance;
import com.example.dataflow.pojo.users.SalesMan;
import com.example.dataflow.pojo.workStation.Branch;
import com.example.dataflow.pojo.workStation.Workstation;
import com.example.dataflow.pojo.workStation.WorkstationList;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {
    @POST("login")
    Observable<LoginStatus> loginGateWay(@Query("uiid") String uuid,
                                         @Query("user_name") String user_name,
                                         @Query("password") String password,
                                         @Query("foundation_name") String foundation_name,
                                         @Query("phone") String phone,
                                         @Query("CreateSource") int createSource,
                                         @Query("appVersion") double appVersion
    );

    @GET("workStations/create")
    Observable<WorkstationList> createWorkstation(@Query("uiid") String uuid,
                                                  @Query("CreateSource") int createSource);

    @POST("workStations")
    Observable<Workstation> insertWorkstation(
            @Query("uiid") String uuid,
            @Query("branch_id") long branchId,
            @Query("workStation_name") String workstationName,
            @Query("CreateSource") int createSource
    );

    @POST("branches")
    @FormUrlEncoded
    Observable<Branch> insertBranch(
            @Field("branch_number") int branchNumber,
            @Field("branch_name") String branchName,
            @Field("uiid") String uuid,
            @Field("CreateSource") int createSource);


    @GET("dealer/getCustomer")
    Observable<Customer> getCustomer(
            @Query("customer_name") String customer_name,
            @Query("uiid") String uuid,
            @Query("WorkerBranchISN") Long WorkerBranchISN,
            @Query("WorkerISN") Long WorkerISN
            );

    @GET("dealer/get_customer_balance")
    Observable<CustomerBalance> getCustomerBalance(
            @Query("uiid") String uuid,
            @Query("Dealer_ISN") String dealerISN,
            @Query("BranchISN") String branchISN,
            @Query("DealerType") String dealerType
            );


    @GET("dealer/getSalesman")
    Observable<SalesMan> getSalesMan(
            @Query("sales_man") String sales_man,
            @Query("uiid") String uuid,
            @Query("WorkerBranchISN") Long WorkerBranchISN,
            @Query("WorkerISN") Long WorkerISN
    );

    @GET("stores/getStores")
    Observable<Stores> getStores(
            @Query("BranchISN") long BranchISN,
            @Query("permission") int permission,
            @Query("uiid") String uuid,
            @Query("CashierStoreBranchISN") Long CashierStoreBranchISN,
            @Query("CashierStoreISN") Long CashierStoreISN);


    @GET("safeDeposit/getSafeDeposit")
    Observable<SafeDeposit> getSafeDeposit(
            @Query("BranchISN") long BranchISN,
            @Query("permission") int permission,
            @Query("uiid") String uuid,
            @Query("SafeDepositBranchISN") Long SafeDepositBranchISN,
            @Query("SafeDepositISN") Long SafeDepositISN);

    @GET("pricesTypes/getPricesTypes")
    Observable<PriceType> getPriceType(
            @Query("permission") int permission,
            @Query("uiid") String uuid,
            @Query("PricesTypeBranchISN") Long PricesTypeBranchISN,
            @Query("PricesTypeISN") Long PricesTypeISN);

    @GET("banks/getBanks")
    Observable<Banks> getBanks(
            @Query("BranchISN") long BranchISN,
            @Query("permission") int permission,
            @Query("uiid") String uuid);


    @GET("product/getProductTest")
    Observable<Product> getProductCustomer(
            @Query("product_name") String productName,
            @Query("uiid") String uuid,
            @Query("PricesTypeBranchISN") long pricesTypeBranchISN,
            @Query("PricesTypeISN") long pricesTypeISN,
            @Query("DealerType") int dealerType,
            @Query("Dealer_ISN") long dealer_ISN,
            @Query("BranchISN") int BranchISN,
            @Query("CurrentBranchISN") int CurrentBranchISN,
            @Query("AllowSpecificDealersPrices") int AllowSpecificDealersPrices,
            @Query("MoveType") int moveType,
            @Query("Serial") String serial
    );

    @GET("product/getProductTest")
    Observable<Product> getProduct(
            @Query("product_name") String productName,
            @Query("uiid") String uuid,
            @Query("PricesTypeBranchISN") long pricesTypeBranchISN,
            @Query("PricesTypeISN") long pricesTypeISN,
            @Query("AllowSpecificDealersPrices") int AllowSpecificDealersPrices,
            @Query("CurrentBranchISN") int CurrentBranchISN,
            @Query("MoveType") int moveType,
            @Query("Serial") String serial
    );


    @POST("invoices/store")
    @FormUrlEncoded
    Observable<InvoiceResponse> placeInvoice(
            @Field("BranchISN") long BranchISN,
            @Field("uiid") String uuid,
            @Field("CashType") int CashType,
            @Field("SaleType") int SaleType,
            @Field("DealerType") int DealerType,
            @Field("DealerBranchISN") int DealerBranchISN,
            @Field("DealerISN") long DealerISN,
            @Field("SalesManBranchISN") long SalesManBranchISN,
            @Field("SalesManISN") long SalesManISN,
            @Field("HeaderNotes") String HeaderNotes,
            @Field("TotalLinesValue") double TotalLinesValue,
            @Field("ServiceValue") double ServiceValue,
            @Field("ServicePer") double ServicePer,
            @Field("DeliveryValue") double DeliveryValue,
            @Field("TotalValueAfterServices") double TotalValueAfterServices,
            @Field("BasicDiscountVal") double BasicDiscountVal,
            @Field("BasicDiscountPer") double BasicDiscountPer,
            @Field("TotalValueAfterDisc") double TotalValueAfterDisc,
            @Field("BasicTaxVal") double BasicTaxVal,
            @Field("BasicTaxPer") double BasicTaxPer,
            @Field("TotalValueAfterTax") double TotalValueAfterTax,
            @Field("NetValue") double NetValue,
            @Field("PaidValue") double PaidValue,
            @Field("RemainValue") double RemainValue,
            @Field("SafeDepositeBranchISN") long SafeDepositeBranchISN,
            @Field("SafeDepositeISN") long SafeDepositeISN,
            @Field("BankBranchISN") long BankBranchISN,
            @Field("BankISN") long BankISN,
            @Field("TableNumber") String TableNumber,
            @Field("DeliveryPhone") String DeliveryPhone,
            @Field("DeliveryAddress") String DeliveryAddress,
            @Field("WorkerCBranchISN") long WorkerCBranchISN,
            @Field("WorkerCISN") long WorkerCISN,
            @Field("CheckNumber") String CheckNumber,
            @Field("CheckDueDate") String CheckDueDate,//HERE
            @Field("CheckBankBranchISN") long CheckBankBranchISN,
            @Field("CheckBankISN") long CheckBankISN,
            @Field("ItemBranchISN[]") ArrayList<Long> ItemBranchISN,
            @Field("ItemISN[]") ArrayList<Long> ItemISN,
            @Field("PriceTypeBranchISN[]") ArrayList<Long> PriceTypeBranchISN,
            @Field("PriceTypeISN[]") ArrayList<Long> PriceTypeISN,
            @Field("StoreBranchISN[]") ArrayList<Long> StoreBranchISN,
            @Field("StoreISN[]") ArrayList<Long> StoreISN,
            @Field("BasicQuantity[]") ArrayList<Float> BasicQuantity,
            @Field("BonusQuantity[]") ArrayList<Float> BonusQuantity,
            @Field("TotalQuantity[]") ArrayList<Float> TotalQuantity,
            @Field("Price[]") ArrayList<Double> Price,
            @Field("MeasureUnitBranchISN[]") ArrayList<Long> MeasureUnitBranchISN,
            @Field("MeasureUnitISN[]") ArrayList<Long> MeasureUnitISN,
            @Field("BasicMeasureUnitBranchISN[]") ArrayList<Long> BasicMeasureUnitBranchISN,
            @Field("BasicMeasureUnitISN[]") ArrayList<Long> BasicMeasureUnitISN,
            @Field("ItemSerial[]") ArrayList<String> ItemSerial,
            @Field("itemExpireDate[]") ArrayList<String> ExpireDateItem,
            @Field("ColorBranchISN[]") ArrayList<Long> ColorBranchISN,
            @Field("ColorISN[]") ArrayList<Long> ColorISN,
            @Field("SizeBranchISN[]") ArrayList<Long> SizeBranchISN,
            @Field("SizeISN[]") ArrayList<Long> SizeISN,
            @Field("SeasonBranchISN[]") ArrayList<Long> SeasonBranchISN,
            @Field("SeasonISN[]") ArrayList<Long> SeasonISN,
            @Field("Group1BranchISN[]") ArrayList<Long> Group1BranchISN,
            @Field("Group1ISN[]") ArrayList<Long> Group1ISN,
            @Field("Group2BranchISN[]") ArrayList<Long> Group2BranchISN,
            @Field("Group2ISN[]") ArrayList<Long> Group2ISN,
            @Field("LineNotes[]") ArrayList<String> LineNotes,
            @Field("numberOFItems") Long numberOFItems,
            @Field("CreateSource") int createSource,
            @Field("NetPrice[]") ArrayList<Double> NetPrice,
            @Field("BasicMeasureUnitQuantity[]") ArrayList<Double> BasicMeasureUnitQuantity,
            @Field("ExpireDate[]") ArrayList<Boolean> ExpireDate,
            @Field("Colors[]") ArrayList<Boolean> Colors,
            @Field("Seasons[]") ArrayList<Boolean> Seasons,
            @Field("Sizes[]") ArrayList<Boolean> Sizes,
            @Field("Serial[]") ArrayList<Boolean> Serial,
            @Field("Group1[]") ArrayList<Boolean> Group1,
            @Field("Group2[]") ArrayList<Boolean> Group2,
            @Field("ServiceItem[]") ArrayList<Boolean> ServiceItem,
            @Field("ItemTax[]") ArrayList<Double> ItemTax,
            @Field("TaxValue[]") ArrayList<Double> TaxValue,
            @Field("TotalLinesTaxVal") Double TotalLinesTaxVal,
            @Field("AllowStoreMinus") Integer allowStoreMinus,
            @Field("ItemName[]") ArrayList<String> itemName,
            @Field("Discount1[]") ArrayList<Double> discount1,
            @Field("AllowStoreMinusConfirm") Integer AllowStoreMinusConfirm,
            @Field("Latitude") float latitude,
            @Field("Longitude") float longitude,
            @Field("invoiceType") String invoiceType,
            @Field("MoveType") Integer moveType,
            @Field("StoreBranchISN2[]") ArrayList<Long> StoreBranchISN2,
            @Field("StoreISN2[]") ArrayList<Long> StoreISN2
    );

    @POST("invoices/check_item_quantity")
    @FormUrlEncoded
    Observable<InvoiceResponse> checkItem(
            @Field("uiid") String uuid,
            @Field("ItemBranchISN[]") ArrayList<Long> ItemBranchISN,
            @Field("ItemISN[]") ArrayList<Long> ItemISN,
            @Field("PriceTypeBranchISN[]") ArrayList<Long> PriceTypeBranchISN,
            @Field("PriceTypeISN[]") ArrayList<Long> PriceTypeISN,
            @Field("StoreBranchISN[]") ArrayList<Long> StoreBranchISN,
            @Field("StoreISN[]") ArrayList<Long> StoreISN,
            @Field("BasicQuantity[]") ArrayList<Float> BasicQuantity,
            @Field("BonusQuantity[]") ArrayList<Float> BonusQuantity,
            @Field("TotalQuantity[]") ArrayList<Float> TotalQuantity,
            @Field("Price[]") ArrayList<Double> Price,
            @Field("MeasureUnitBranchISN[]") ArrayList<Long> MeasureUnitBranchISN,
            @Field("MeasureUnitISN[]") ArrayList<Long> MeasureUnitISN,
            @Field("BasicMeasureUnitBranchISN[]") ArrayList<Long> BasicMeasureUnitBranchISN,
            @Field("BasicMeasureUnitISN[]") ArrayList<Long> BasicMeasureUnitISN,
            @Field("ItemSerial[]") ArrayList<String> ItemSerial,
            @Field("itemExpireDate[]") ArrayList<String> ExpireDateItem,
            @Field("ColorBranchISN[]") ArrayList<Long> ColorBranchISN,
            @Field("ColorISN[]") ArrayList<Long> ColorISN,
            @Field("SizeBranchISN[]") ArrayList<Long> SizeBranchISN,
            @Field("SizeISN[]") ArrayList<Long> SizeISN,
            @Field("SeasonBranchISN[]") ArrayList<Long> SeasonBranchISN,
            @Field("SeasonISN[]") ArrayList<Long> SeasonISN,
            @Field("Group1BranchISN[]") ArrayList<Long> Group1BranchISN,
            @Field("Group1ISN[]") ArrayList<Long> Group1ISN,
            @Field("Group2BranchISN[]") ArrayList<Long> Group2BranchISN,
            @Field("Group2ISN[]") ArrayList<Long> Group2ISN,
            @Field("LineNotes[]") ArrayList<String> LineNotes,
            @Field("NetPrice[]") ArrayList<Double> NetPrice,
            @Field("BasicMeasureUnitQuantity[]") ArrayList<Double> BasicMeasureUnitQuantity,
            @Field("ExpireDate[]") ArrayList<Boolean> ExpireDate,
            @Field("Colors[]") ArrayList<Boolean> Colors,
            @Field("Seasons[]") ArrayList<Boolean> Seasons,
            @Field("Sizes[]") ArrayList<Boolean> Sizes,
            @Field("Serial[]") ArrayList<Boolean> Serial,
            @Field("Group1[]") ArrayList<Boolean> Group1,
            @Field("Group2[]") ArrayList<Boolean> Group2,
            @Field("ServiceItem[]") ArrayList<Boolean> ServiceItem,
            @Field("ItemTax[]") ArrayList<Double> ItemTax,
            @Field("TaxValue[]") ArrayList<Double> TaxValue,
            @Field("ItemName[]") ArrayList<String> itemName,
            @Field("Discount1[]") ArrayList<Double> discount1,
            @Field("AllowStoreMinus") Integer allowStoreMinus,
            @Field("AllowStoreMinusConfirm") Integer AllowStoreMinusConfirm
    );


    @GET("invoices/query")
    Observable<Invoice> getPrintingData(
            @Query("BranchISN") String branchISN,
            @Query("uiid") String uuid,
            @Query("Move_ID") String Move_ID,
            @Query("WorkerCBranchISN") String WorkerCBranchISN,
            @Query("WorkerCISN") String WorkerCISN,
            @Query("permission") Integer permission,
            @Query("MoveType") Integer MoveType

    );


    @GET("reports/warehouse_report")
    Observable<StoreReportModel> getStoresReport(
            @Query("uiid") String uuid,
            @Query("StoreBranchISN") int storeBranchISN,
            @Query("StoreISN") int storeISN);


    @POST("invoices/cash_receipt")
    @FormUrlEncoded
    Observable<ReceiptResponse> createReceipt(
            @Field("BranchISN") long BranchISN,
            @Field("uiid") String uuid,
            @Field("CashType") int CashType,
            @Field("SaleType") int SaleType,
            @Field("DealerType") int DealerType,
            @Field("DealerBranchISN") int DealerBranchISN,
            @Field("DealerISN") long DealerISN,
            @Field("SalesManBranchISN") long SalesManBranchISN,
            @Field("SalesManISN") long SalesManISN,
            @Field("HeaderNotes") String HeaderNotes,
            @Field("TotalLinesValue") double TotalLinesValue,
            @Field("ServiceValue") double ServiceValue,
            @Field("ServicePer") double ServicePer,
            @Field("DeliveryValue") double DeliveryValue,
            @Field("TotalValueAfterServices") double TotalValueAfterServices,
            @Field("BasicDiscountVal") double BasicDiscountVal,
            @Field("BasicDiscountPer") double BasicDiscountPer,
            @Field("TotalValueAfterDisc") double TotalValueAfterDisc,
            @Field("BasicTaxVal") double BasicTaxVal,
            @Field("BasicTaxPer") double BasicTaxPer,
            @Field("TotalValueAfterTax") double TotalValueAfterTax,
            @Field("NetValue") double NetValue,
            @Field("PaidValue") double PaidValue,
            @Field("RemainValue") double RemainValue,
            @Field("SafeDepositeBranchISN") long SafeDepositeBranchISN,
            @Field("SafeDepositeISN") long SafeDepositeISN,
            @Field("BankBranchISN") long BankBranchISN,
            @Field("BankISN") long BankISN,
            @Field("TableNumber") String TableNumber,
            @Field("DeliveryPhone") String DeliveryPhone,
            @Field("DeliveryAddress") String DeliveryAddress,
            @Field("WorkerCBranchISN") long WorkerCBranchISN,
            @Field("WorkerCISN") long WorkerCISN,
            @Field("CheckNumber") String CheckNumber,
            @Field("CheckDueDate") String CheckDueDate,//HERE
            @Field("CheckBankBranchISN") long CheckBankBranchISN,
            @Field("CheckBankISN") long CheckBankISN,
            @Field("CreateSource") int createSource,
            @Field("Latitude") float latitude,
            @Field("Longitude") float longitude
    );

    @GET("invoices/print_cash_receipt")
    Observable<ReceiptModel> getReceipt(
            @Query("BranchISN") long BranchISN,
            @Query("uiid") String uiid,
            @Query("Move_ID") String Move_ID,
            @Query("WorkerCBranchISN") long WorkerCBranchISN,
            @Query("WorkerCISN") long WorkerCISN,
            @Query("permission") int permission);


    @GET("branches")
    Observable<Branches> getBranches(@Query("uiid") String uuid);

    @GET("workers")
    Observable<WorkersResponse> getWorkers(@Query("uiid") String uuid);

    @POST("reports/money_report")
    Observable<FinancialReportResponse> getFinancialReport(@Body ReportBody reportBody, @Query("uiid") String uuid,
                                                           @Query("StoreBranchISN") long storeBranchISN,
                                                           @Query("StoreISN") long storeISN,
                                                           @Query("WorkerBranchISN") long workerBranchISN);
}
