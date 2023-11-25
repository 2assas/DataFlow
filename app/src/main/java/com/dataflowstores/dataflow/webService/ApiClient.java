package com.dataflowstores.dataflow.webService;

import com.dataflowstores.dataflow.pojo.expenses.ExpensesResponse;
import com.dataflowstores.dataflow.pojo.expenses.MainExpResponse;
import com.dataflowstores.dataflow.pojo.expenses.SubExpResponse;
import com.dataflowstores.dataflow.pojo.expenses.WorkerResponse;
import com.dataflowstores.dataflow.pojo.financialReport.FinancialReportResponse;
import com.dataflowstores.dataflow.pojo.financialReport.ReportBody;
import com.dataflowstores.dataflow.pojo.invoice.Invoice;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceBody;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceResponse;
import com.dataflowstores.dataflow.pojo.login.LoginStatus;
import com.dataflowstores.dataflow.pojo.product.Product;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptModel;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptResponse;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.report.StoreReportModel;
import com.dataflowstores.dataflow.pojo.report.WorkersResponse;
import com.dataflowstores.dataflow.pojo.report.cashierMoves.CashierMovesReportResponse;
import com.dataflowstores.dataflow.pojo.report.cashierMoves.moveTypes.MoveTypesResponse;
import com.dataflowstores.dataflow.pojo.report.itemSalesReport.ItemSalesResponse;
import com.dataflowstores.dataflow.pojo.searchItemPrice.ItemPriceResponse;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.PriceType;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;
import com.dataflowstores.dataflow.pojo.users.Customer;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.pojo.users.SalesMan;
import com.dataflowstores.dataflow.pojo.workStation.Branch;
import com.dataflowstores.dataflow.pojo.workStation.Workstation;
import com.dataflowstores.dataflow.pojo.workStation.WorkstationList;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiClient {
    @POST("login")
    Observable<LoginStatus> loginGateWay(@Query("uiid") String uuid, @Query("user_name") String user_name, @Query("password") String password, @Query("foundation_name") String foundation_name, @Query("phone") String phone, @Query("CreateSource") int createSource, @Query("appVersion") double appVersion, @Query("SelectedBranchISN") String SelectedBranchISN, @Query("SelectedSafeDepositBranchISN") String SelectedSafeDepositBranchISN, @Query("SelectedSafeDepositISN") String SelectedSafeDepositISN, @Query("SelectedStoreBranchISN") String SelectedStoreBranchISN, @Query("SelectedStoreISN") String SelectedStoreISN, @Query("Demo") int Demo, @Query("SelectedFoundation") int selectedFoundation);

    @GET("workStations/create")
    Observable<WorkstationList> createWorkstation(@Query("uiid") String uuid, @Query("CreateSource") int createSource, @Query("SelectedFoundation") int selectedFoundation
    );

    @POST("workStations")
    Observable<Workstation> insertWorkstation(@Query("uiid") String uuid, @Query("branch_id") long branchId, @Query("workStation_name") String workstationName, @Query("CreateSource") int createSource, @Query("SelectedFoundation") int selectedFoundation
    );

    @POST("branches")
    @FormUrlEncoded
    Observable<Branch> insertBranch(@Field("branch_number") int branchNumber, @Field("branch_name") String branchName, @Field("uiid") String uuid, @Field("CreateSource") int createSource, @Query("SelectedFoundation") int selectedFoundation
    );


    @GET("dealer/getCustomer")
    Observable<Customer> getCustomer(@Query("customer_name") String customer_name, @Query("uiid") String uuid, @Query("WorkerBranchISN") Long WorkerBranchISN, @Query("WorkerISN") Long WorkerISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("dealer/get_customer_balance")
    Observable<CustomerBalance> getCustomerBalance(@Query("uiid") String uuid, @Query("Dealer_ISN") String dealerISN, @Query("BranchISN") String branchISN, @Query("DealerType") String dealerType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );


    @GET("dealer/getSalesman")
    Observable<SalesMan> getSalesMan(@Query("sales_man") String sales_man, @Query("uiid") String uuid, @Query("WorkerBranchISN") Long WorkerBranchISN, @Query("WorkerISN") Long WorkerISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("stores/getStores")
    Observable<Stores> getStores(@Query("BranchISN") Long BranchISN, @Query("permission") int permission, @Query("uiid") String uuid, @Query("CashierStoreBranchISN") Long CashierStoreBranchISN, @Query("CashierStoreISN") Long CashierStoreISN, @Query("AllBranchesWorker") int AllBranchesWorker, @Query("MoveType") int MoveType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );


    @GET("safeDeposit/getSafeDeposit")
    Observable<SafeDeposit> getSafeDeposit(@Query("BranchISN") Long BranchISN, @Query("permission") int permission, @Query("uiid") String uuid, @Query("SafeDepositBranchISN") Long SafeDepositBranchISN, @Query("SafeDepositISN") Long SafeDepositISN, @Query("AllBranchesWorker") int AllBranchesWorker, @Query("MoveType") int MoveType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("pricesTypes/getPricesTypes")
    Observable<PriceType> getPriceType(@Query("permission") int permission, @Query("uiid") String uuid, @Query("PricesTypeBranchISN") Long PricesTypeBranchISN, @Query("PricesTypeISN") Long PricesTypeISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("banks/getBanks")
    Observable<Banks> getBanks(@Query("BranchISN") long BranchISN, @Query("permission") int permission, @Query("uiid") String uuid, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);


    @GET("product/getProductTest")
    Observable<Product> getProductCustomer(@Query("product_name") String productName, @Query("uiid") String uuid, @Query("PricesTypeBranchISN") long pricesTypeBranchISN, @Query("PricesTypeISN") long pricesTypeISN, @Query("DealerType") int dealerType, @Query("Dealer_ISN") long dealer_ISN, @Query("BranchISN") int BranchISN, @Query("CurrentBranchISN") int CurrentBranchISN, @Query("AllowSpecificDealersPrices") int AllowSpecificDealersPrices, @Query("MoveType") int moveType, @Query("Serial") String serial, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("product/getProductTest")
    Observable<Product> getProduct(@Query("product_name") String productName, @Query("uiid") String uuid, @Query("PricesTypeBranchISN") long pricesTypeBranchISN, @Query("PricesTypeISN") long pricesTypeISN, @Query("AllowSpecificDealersPrices") int AllowSpecificDealersPrices, @Query("CurrentBranchISN") int CurrentBranchISN, @Query("MoveType") int moveType, @Query("Serial") String serial, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @POST("invoices/store2")
    Observable<InvoiceResponse> placeInvoice(@Body InvoiceBody invoiceBody);

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
            @Field("Group2ISN[]") ArrayList<Long> Group2ISN, @Field("LineNotes[]") ArrayList<String> LineNotes, @Field("NetPrice[]") ArrayList<Double> NetPrice, @Field("BasicMeasureUnitQuantity[]") ArrayList<Double> BasicMeasureUnitQuantity, @Field("ExpireDate[]") ArrayList<Boolean> ExpireDate, @Field("Colors[]") ArrayList<Boolean> Colors, @Field("Seasons[]") ArrayList<Boolean> Seasons, @Field("Sizes[]") ArrayList<Boolean> Sizes, @Field("Serial[]") ArrayList<Boolean> Serial, @Field("Group1[]") ArrayList<Boolean> Group1, @Field("Group2[]") ArrayList<Boolean> Group2, @Field("ServiceItem[]") ArrayList<Boolean> ServiceItem, @Field("ItemTax[]") ArrayList<Double> ItemTax, @Field("TaxValue[]") ArrayList<Double> TaxValue, @Field("ItemName[]") ArrayList<String> itemName, @Field("Discount1[]") ArrayList<Double> discount1, @Field("AllowStoreMinus") Integer allowStoreMinus, @Field("AllowStoreMinusConfirm") Integer AllowStoreMinusConfirm, @Field("WorkerName") String WorkerName, @Field("user_name") String user_name, @Field("WorkStationName") String WorkStationName, @Field("WorkStation_ISN") String WorkStation_ISN, @Field("WorkStationBranchISN") String WorkStationBranchISN, @Field("SelectedFoundation") int selectedFoundation
            , @Field("LogIn_BISN") String LogIn_BISN
            , @Field("LogIn_UID") String LogIn_UID
            , @Field("LogIn_WBISN") String LogIn_WBISN
            , @Field("LogIn_WISN") String LogIn_WISN
            , @Field("LogIn_WName") String LogIn_WName
            , @Field("LogIn_WSBISN") String LogIn_WSBISN
            , @Field("LogIn_WSISN") String LogIn_WSISN
            , @Field("LogIn_WSName") String LogIn_WSName
            , @Field("LogIn_CS") String LogIn_CS
            , @Field("LogIn_VN") String LogIn_VN
            , @Field("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("invoices/query")
    Observable<Invoice> getPrintingData(@Query("BranchISN") String branchISN, @Query("uiid") String uuid, @Query("Move_ID") String Move_ID, @Query("WorkerCBranchISN") String WorkerCBranchISN, @Query("WorkerCISN") String WorkerCISN, @Query("permission") Integer permission, @Query("MoveType") Integer MoveType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("reports/warehouse_report")
    Observable<StoreReportModel> getStoresReport(@Query("uiid") String uuid, @Query("StoreBranchISN") Integer storeBranchISN, @Query("StoreISN") Integer storeISN, @Query("ItemBranchISN") Integer ItemBranchISN, @Query("ItemISN") Integer ItemISN, @Query("AvailableOnly") Integer AvailableOnly, @Query("ItemName") String itemName, @Query("MoveType") int moveType, @Query("WorkerName") String WorkerName, @Query("user_name") String user_name, @Query("WorkStationName") String WorkStationName, @Query("WorkStation_ISN") String WorkStation_ISN, @Query("WorkStationBranchISN") String WorkStationBranchISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    // TODO:: complete error handling..
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
            @Field("PaidValue") double PaidValue, @Field("RemainValue") double RemainValue, @Field("SafeDepositeBranchISN") long SafeDepositeBranchISN, @Field("SafeDepositeISN") long SafeDepositeISN, @Field("BankBranchISN") long BankBranchISN, @Field("BankISN") long BankISN, @Field("TableNumber") String TableNumber, @Field("DeliveryPhone") String DeliveryPhone, @Field("DeliveryAddress") String DeliveryAddress, @Field("WorkerCBranchISN") long WorkerCBranchISN, @Field("WorkerCISN") long WorkerCISN, @Field("CheckNumber") String CheckNumber, @Field("CheckDueDate") String CheckDueDate,//HERE
            @Field("CheckBankBranchISN") long CheckBankBranchISN, @Field("CheckBankISN") long CheckBankISN, @Field("CreateSource") int createSource, @Field("Latitude") float latitude, @Field("Longitude") float longitude, @Field("WorkerName") String WorkerName, @Field("user_name") String user_name, @Field("WorkStationName") String WorkStationName, @Field("WorkStation_ISN") String WorkStation_ISN, @Field("WorkStationBranchISN") String WorkStationBranchISN, @Query("SelectedFoundation") int selectedFoundation
            , @Field("LogIn_BISN") String LogIn_BISN
            , @Field("LogIn_UID") String LogIn_UID
            , @Field("LogIn_WBISN") String LogIn_WBISN
            , @Field("LogIn_WISN") String LogIn_WISN
            , @Field("LogIn_WName") String LogIn_WName
            , @Field("LogIn_WSBISN") String LogIn_WSBISN
            , @Field("LogIn_WSISN") String LogIn_WSISN
            , @Field("LogIn_WSName") String LogIn_WSName
            , @Field("LogIn_CS") String LogIn_CS
            , @Field("LogIn_VN") String LogIn_VN
            , @Field("LogIn_FAlternative") String LogIn_FAlternative
    );

    @POST("invoices/Create_New_Expenses")
    @FormUrlEncoded
    Observable<ReceiptResponse> createExpense(
            @Field("BranchISN") long BranchISN,
            @Field("uiid") String uuid,
            @Field("CashType") int CashType,
            @Field("SaleType") int SaleType,
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
            @Field("WorkerCBranchISN") long WorkerCBranchISN, @Field("WorkerCISN") long WorkerCISN, @Field("CheckNumber") String CheckNumber, @Field("CheckDueDate") String CheckDueDate,//HERE
            @Field("CheckBankBranchISN") long CheckBankBranchISN, @Field("CheckBankISN") long CheckBankISN, @Field("CreateSource") int createSource, @Field("Latitude") float latitude, @Field("Longitude") float longitude, @Field("ShiftISN") Long ShiftISN, @Field("MainExpMenuISN") Long MainExpMenuISN, @Field("MainExpMenuBranchISN") Long MainExpMenuBranchISN, @Field("MainExpMenuName") String MainExpMenuName, @Field("SubExpMenuISN") Long SubExpMenuISN, @Field("SubExpMenuBranchISN") Long SubExpMenuBranchISN, @Field("SubExpMenuName") String SubExpMenuName, @Field("SelectedWorkerBranchISN") Long SelectedWorkerBranchISN, @Field("SelectedWorkerISN") Long SelectedWorkerISN, @Field("WorkerName") String WorkerName, @Field("user_name") String user_name, @Field("WorkStationName") String WorkStationName, @Field("WorkStation_ISN") String WorkStation_ISN, @Field("WorkStationBranchISN") String WorkStationBranchISN, @Query("SelectedFoundation") int selectedFoundation
            , @Field("LogIn_BISN") String LogIn_BISN
            , @Field("LogIn_UID") String LogIn_UID
            , @Field("LogIn_WBISN") String LogIn_WBISN
            , @Field("LogIn_WISN") String LogIn_WISN
            , @Field("LogIn_WName") String LogIn_WName
            , @Field("LogIn_WSBISN") String LogIn_WSBISN
            , @Field("LogIn_WSISN") String LogIn_WSISN
            , @Field("LogIn_WSName") String LogIn_WSName
            , @Field("LogIn_CS") String LogIn_CS
            , @Field("LogIn_VN") String LogIn_VN
            , @Field("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("invoices/print_cash_receipt")
    Observable<ReceiptModel> getReceipt(@Query("BranchISN") long BranchISN, @Query("uiid") String uiid, @Query("Move_ID") String Move_ID, @Query("WorkerCBranchISN") long WorkerCBranchISN, @Query("WorkerCISN") long WorkerCISN, @Query("permission") int permission, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );


    @GET("branches")
    Observable<Branches> getBranches(@Query("uiid") String uuid, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("workers")
    Observable<WorkersResponse> getWorkers(@Query("uiid") String uuid, @Query("BranchISN") long branchISN, @Query("WorkerCBranchISN") long workerBranchISN, @Query("WorkerCISN") long workerISN, @Query("permission") int permission, @Query("MoveType") int moveType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @POST("reports/money_report")
    Observable<FinancialReportResponse> getFinancialReport(@Body ReportBody reportBody, @Query("uiid") String uuid, @Query("StoreBranchISN") long storeBranchISN, @Query("StoreISN") long storeISN, @Query("WorkerBranchISN") long workerBranchISN, @Query("WorkerName") String WorkerName, @Query("user_name") String user_name, @Query("WorkStationName") String WorkStationName, @Query("WorkStation_ISN") String WorkStation_ISN, @Query("WorkStationBranchISN") String WorkStationBranchISN, @Query("WorkerCISN") String WorkerCISN, @Query("WorkerCBranchISN") String WorkerCBranchISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @POST("reports/CashierMoves_Report")
    Observable<CashierMovesReportResponse> getCashierMovesReport(@Body ReportBody reportBody, @Query("uiid") String uuid, @Query("StoreBranchISN") long storeBranchISN, @Query("StoreISN") long storeISN, @Query("WorkerBranchISN") long workerBranchISN, @Query("WorkerName") String WorkerName, @Query("user_name") String user_name, @Query("WorkStationName") String WorkStationName, @Query("WorkStation_ISN") String WorkStation_ISN, @Query("WorkStationBranchISN") String WorkStationBranchISN, @Query("MoveType") Integer MoveType, @Query("DealerType") Integer DealerType, @Query("DealerBranchISN") Integer DealerBranchISN, @Query("Dealer_ISN") Long Dealer_ISN, @Query("WorkerCISN") String WorkerCISN, @Query("WorkerCBranchISN") Long WorkerCBranchISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("reports/AllMoves_Types")
    Observable<MoveTypesResponse> getMoveTypes(@Query("uiid") String uuid, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("reports/itemssales_report")
    Observable<ItemSalesResponse> getItemSalesReport(@Query("uiid") String uuid, @Query("Branch_ISN") Long storeBranchISN, @Query("from_workday") String from_workday, @Query("to_workday") String to_workday, @Query("Shift_ISN") String Shift_ISN, @Query("WorkerBranchISN") Long workerBranchISN, @Query("Worker_ISN") String Worker_ISN, @Query("from") String from, @Query("to") String to, @Query("VendorID") Long vendorId, @Query("WorkerCISN") String WorkerCISN, @Query("WorkerCBranchISN") String WorkerCBranchISN, @Query("WorkerName") String WorkerName, @Query("user_name") String user_name, @Query("WorkStationName") String WorkStationName, @Query("WorkStation_ISN") String WorkStation_ISN, @Query("WorkStationBranchISN") String WorkStationBranchISN, @Query("DealerType") Integer DealerType, @Query("DealerBranchISN") Long DealerBranchISN, @Query("Dealer_ISN") Long Dealer_ISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    //
    @GET("invoices/Get_Main_Expenses")
    Observable<MainExpResponse> getMainExpenses(@Query("uiid") String uiid, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("workers")
    Observable<WorkerResponse> getExpWorkers(@Query("uiid") String uiid, @Query("BranchISN") long branchISN, @Query("WorkerCBranchISN") long workerBranchISN, @Query("WorkerCISN") long workerISN, @Query("permission") int permission, @Query("MoveType") int moveType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);

    @GET("invoices/Get_Sub_Expenses")
    Observable<SubExpResponse> getSubExpenses(@Query("uiid") String uiid, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative);


    @GET("invoices/print_Expenses")
    Observable<ExpensesResponse> getExpenses(@Query("BranchISN") long BranchISN, @Query("uiid") String uiid, @Query("Move_ID") String Move_ID, @Query("WorkerCBranchISN") long WorkerCBranchISN, @Query("WorkerCISN") long WorkerCISN, @Query("permission") int permission, @Query("moveType") int moveType, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );

    @GET("invoices/Get_Item_Prices")
    Observable<ItemPriceResponse> getItemPrice(@Query("uiid") String uiid, @Query("ItemBranchISN") int itemBranchISN, @Query("ItemISN") int itemISN, @Query("WorkerName") String WorkerName, @Query("user_name") String user_name, @Query("WorkStationName") String WorkStationName, @Query("WorkStation_ISN") String WorkStation_ISN, @Query("WorkStationBranchISN") String WorkStationBranchISN, @Query("SelectedFoundation") int selectedFoundation
            , @Query("LogIn_BISN") String LogIn_BISN
            , @Query("LogIn_UID") String LogIn_UID
            , @Query("LogIn_WBISN") String LogIn_WBISN
            , @Query("LogIn_WISN") String LogIn_WISN
            , @Query("LogIn_WName") String LogIn_WName
            , @Query("LogIn_WSBISN") String LogIn_WSBISN
            , @Query("LogIn_WSISN") String LogIn_WSISN
            , @Query("LogIn_WSName") String LogIn_WSName
            , @Query("LogIn_CS") String LogIn_CS
            , @Query("LogIn_VN") String LogIn_VN
            , @Query("LogIn_FAlternative") String LogIn_FAlternative
    );


}
