package com.dataflowstores.dataflow.ViewModels;

import static com.dataflowstores.dataflow.App.selectedFoundation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceBody;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceResponse;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.utils.SingleLiveEvent;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class CheckoutVM extends ViewModel {
    public SingleLiveEvent<InvoiceResponse> responseDataMutableLiveData = new SingleLiveEvent<>();
    public MutableLiveData<InvoiceResponse> checkItemMutableLiveData = new MutableLiveData<>();
    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();

    public void placeInvoice(long BranchISN, String uuid, int CashType, int SaleType, int DealerType, int DealerBranchISN, long DealerISN, long SalesManBranchISN,
                             long SalesManISN, String HeaderNotes, double TotalLinesValue, double ServiceValue, double ServicePer, double DeliveryValue,
                             double TotalValueAfterServices, double BasicDiscountVal, double BasicDiscountPer, double TotalValueAfterDisc, double BasicTaxVal,
                             double BasicTaxPer, double TotalValueAfterTax, double NetValue, double PaidValue, double RemainValue, long SafeDepositeBranchISN, long SafeDepositeISN, long BankBranchISN,
                             long BankISN, String TableNumber, String DeliveryPhone, String DeliveryAddress, long WorkerCBranchISN, long WorkerCISN, String CheckNumber,
                             String CheckDueDate, long CheckBankBranchISN, long CheckBankISN, ArrayList<Long> ItemBranchISN, ArrayList<Long> ItemISN, ArrayList<Long> PriceTypeBranchISN,
                             ArrayList<Long> PriceTypeISN, ArrayList<Long> StoreBranchISN, ArrayList<Long> StoreISN, ArrayList<Float> BasicQuantity, ArrayList<Float> BonusQuantity,
                             ArrayList<Float> TotalQuantity, ArrayList<Double> Price, ArrayList<Long> MeasureUnitBranchISN, ArrayList<Long> MeasureUnitISN, ArrayList<Long> BasicMeasureUnitBranchISN,
                             ArrayList<Long> BasicMeasureUnitISN, ArrayList<String> ItemSerial, ArrayList<String> ExpireDate, ArrayList<Long> ColorBranchISN, ArrayList<Long> ColorISN, ArrayList<Long> SizeBranchISN,
                             ArrayList<Long> SizeISN, ArrayList<Long> SeasonBranchISN, ArrayList<Long> SeasonISN, ArrayList<Long> Group1BranchISN, ArrayList<Long> Group1ISN,
                             ArrayList<Long> Group2BranchISN, ArrayList<Long> Group2ISN, ArrayList<String> LineNotes, Long numberOFItems, ArrayList<Double> NetPrice, ArrayList<Double> BasicMeasureUnitQuantity,
                             ArrayList<Boolean> ExpireDateBool, ArrayList<Boolean> ColorsBool, ArrayList<Boolean> SeasonsBool, ArrayList<Boolean> SizesBool, ArrayList<Boolean> SerialBool,
                             ArrayList<Boolean> Group1Bool, ArrayList<Boolean> Group2Bool, ArrayList<Boolean> ServiceItem, ArrayList<Double> ItemTax, ArrayList<Double> TaxValue, double TotalLinesTaxVal, int allowStoreMinus,
                             ArrayList<String> itemName, ArrayList<Double> discount1, Integer AllowStoreMinusConfirm, float latitude, float longitude, String invoiceType, Integer moveType, ArrayList<Long> StoreBranchISN2, ArrayList<Long> StoreISN2,
                             String BranchISNStockMove, ArrayList<Integer> productAllowStoreMinus,  ArrayList<String> productStoreName,
                             ArrayList<Double> illustrativeQuantity, Double customerDiscount) {
        Log.e("checkInvoice", "Triggered request");

        Observable<InvoiceResponse> invoiceObsevable = apiClient.placeInvoice(new
                        InvoiceBody(BranchISN, uuid, CashType, SaleType, DealerType, DealerBranchISN, DealerISN, SalesManBranchISN, SalesManISN, HeaderNotes, TotalLinesValue, ServiceValue, ServicePer, DeliveryValue, TotalValueAfterServices, BasicDiscountVal, BasicDiscountPer, TotalValueAfterDisc, BasicTaxVal, BasicTaxPer, TotalValueAfterTax, NetValue, PaidValue, RemainValue, SafeDepositeBranchISN, SafeDepositeISN, BankBranchISN, BankISN, TableNumber, DeliveryPhone, DeliveryAddress, WorkerCBranchISN, WorkerCISN, CheckNumber, CheckDueDate, CheckBankBranchISN, CheckBankISN, ItemBranchISN, ItemISN, PriceTypeBranchISN, PriceTypeISN, StoreBranchISN, StoreISN, BasicQuantity, BonusQuantity, TotalQuantity, Price, MeasureUnitBranchISN, MeasureUnitISN, BasicMeasureUnitBranchISN, BasicMeasureUnitISN, ItemSerial, ExpireDate, ColorBranchISN, ColorISN, SizeBranchISN, SizeISN, SeasonBranchISN, SeasonISN, Group1BranchISN, Group1ISN, Group2BranchISN, Group2ISN, LineNotes, numberOFItems, 2, NetPrice, BasicMeasureUnitQuantity, ExpireDateBool, ColorsBool, SeasonsBool, SizesBool, SerialBool, Group1Bool, Group2Bool, ServiceItem, ItemTax, TaxValue, TotalLinesTaxVal, allowStoreMinus, itemName, discount1, AllowStoreMinusConfirm, latitude, longitude, invoiceType, moveType, StoreBranchISN2, StoreISN2, App.currentUser.getWorkerName(), App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()), BranchISNStockMove, selectedFoundation, App.currentUser.getLogIn_BISN(), App.currentUser.getLogIn_UID(), App.currentUser.getLogIn_WBISN(), App.currentUser.getLogIn_WISN(), App.currentUser.getLogIn_WName(), App.currentUser.getLogIn_WSBISN(), App.currentUser.getLogIn_WSISN(), App.currentUser.getLogIn_WSName(), App.currentUser.getLogIn_CS(), App.currentUser.getLogIn_VN(), App.currentUser.getLogIn_FAlternative(), productAllowStoreMinus,productStoreName
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6(),
                        illustrativeQuantity, customerDiscount
                ),App.currentUser.getIllustrativeQuantity(), App.currentUser.getVendorID(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Disposable invoiceDisposable = invoiceObsevable.subscribe(invoiceResponse -> {
            responseDataMutableLiveData.setValue(invoiceResponse);

        },throwable -> {
            if (throwable instanceof IOException) {
                //handle network error
                toastErrorMutableLiveData.postValue("No Internet Connection!");
            }
            else if (throwable instanceof HttpException) {
                ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                try {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //handle HTTP error response code
            } else {
                //handle other exceptions
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
        });
        compositeDisposable.add(invoiceDisposable);
    }


    public void checkItem(String uuid, ArrayList<Long> ItemBranchISN, ArrayList<Long> ItemISN, ArrayList<Long> PriceTypeBranchISN,
                          ArrayList<Long> PriceTypeISN, ArrayList<Long> StoreBranchISN, ArrayList<Long> StoreISN, ArrayList<Float> BasicQuantity, ArrayList<Float> BonusQuantity,
                          ArrayList<Float> TotalQuantity, ArrayList<Double> Price, ArrayList<Long> MeasureUnitBranchISN, ArrayList<Long> MeasureUnitISN, ArrayList<Long> BasicMeasureUnitBranchISN,
                          ArrayList<Long> BasicMeasureUnitISN, ArrayList<String> ItemSerial, ArrayList<String> ExpireDate, ArrayList<Long> ColorBranchISN, ArrayList<Long> ColorISN, ArrayList<Long> SizeBranchISN,
                          ArrayList<Long> SizeISN, ArrayList<Long> SeasonBranchISN, ArrayList<Long> SeasonISN, ArrayList<Long> Group1BranchISN, ArrayList<Long> Group1ISN,
                          ArrayList<Long> Group2BranchISN, ArrayList<Long> Group2ISN, ArrayList<String> LineNotes, ArrayList<Double> NetPrice, ArrayList<Double> BasicMeasureUnitQuantity,
                          ArrayList<Boolean> ExpireDateBool, ArrayList<Boolean> ColorsBool, ArrayList<Boolean> SeasonsBool, ArrayList<Boolean> SizesBool, ArrayList<Boolean> SerialBool,
                          ArrayList<Boolean> Group1Bool, ArrayList<Boolean> Group2Bool, ArrayList<Boolean> ServiceItem, ArrayList<Double> ItemTax, ArrayList<Double> TaxValue,
                          ArrayList<String> itemName, ArrayList<Double> discount1, int allowStoreMinus, Integer AllowStoreMinusConfirm, Integer allowCurrentStoreMinus) {

        Observable<InvoiceResponse> checkItem = apiClient.checkItem(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid, ItemBranchISN, ItemISN, PriceTypeBranchISN, PriceTypeISN, StoreBranchISN, StoreISN, BasicQuantity, BonusQuantity, TotalQuantity, Price, MeasureUnitBranchISN, MeasureUnitISN, BasicMeasureUnitBranchISN, BasicMeasureUnitISN, ItemSerial, ExpireDate, ColorBranchISN, ColorISN, SizeBranchISN, SizeISN, SeasonBranchISN, SeasonISN, Group1BranchISN, Group1ISN, Group2BranchISN, Group2ISN, LineNotes, NetPrice, BasicMeasureUnitQuantity, ExpireDateBool, ColorsBool, SeasonsBool, SizesBool, SerialBool, Group1Bool, Group2Bool, ServiceItem, ItemTax, TaxValue, itemName, discount1, allowStoreMinus, AllowStoreMinusConfirm, App.currentUser.getWorkerName(), App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()), selectedFoundation, App.currentUser.getLogIn_BISN(), App.currentUser.getLogIn_UID(), App.currentUser.getLogIn_WBISN(), App.currentUser.getLogIn_WISN(), App.currentUser.getLogIn_WName(), App.currentUser.getLogIn_WSBISN(), App.currentUser.getLogIn_WSISN(), App.currentUser.getLogIn_WSName(), App.currentUser.getLogIn_CS(), App.currentUser.getLogIn_VN(), App.currentUser.getLogIn_FAlternative(),allowCurrentStoreMinus
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6()
                )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Disposable checkItemDisposable =   checkItem.subscribe(invoiceResponse -> {
            checkItemMutableLiveData.setValue(invoiceResponse);

        },throwable -> {
            if (throwable instanceof IOException) {
                //handle network error
                toastErrorMutableLiveData.postValue("No Internet Connection!");
            } else if (throwable instanceof HttpException) {
                ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                try {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //handle HTTP error response code
            } else {
                //handle other exceptions
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
            Log.e("ERRORR !!", String.valueOf(throwable));

        });
        compositeDisposable.add(checkItemDisposable);
    }

}
