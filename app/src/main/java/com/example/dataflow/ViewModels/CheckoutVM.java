package com.example.dataflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dataflow.pojo.invoice.InvoiceResponse;
import com.example.dataflow.pojo.users.CustomerBalance;
import com.example.dataflow.utils.SingleLiveEvent;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckoutVM extends ViewModel {
    public SingleLiveEvent<InvoiceResponse> responseDataMutableLiveData= new SingleLiveEvent<>();
    public MutableLiveData<InvoiceResponse> checkItemMutableLiveData= new MutableLiveData<>();
    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();


    public void placeInvoice(long BranchISN, String uuid, int CashType, int SaleType, int DealerType, int DealerBranchISN, long DealerISN, long SalesManBranchISN,
            long SalesManISN, String HeaderNotes, double TotalLinesValue, double ServiceValue, double ServicePer, double DeliveryValue,
            double TotalValueAfterServices,double BasicDiscountVal, double BasicDiscountPer, double TotalValueAfterDisc,double BasicTaxVal,
            double BasicTaxPer,double TotalValueAfterTax, double NetValue, double PaidValue, double RemainValue, long SafeDepositeBranchISN, long SafeDepositeISN, long BankBranchISN,
            long BankISN, String TableNumber, String DeliveryPhone, String DeliveryAddress, long WorkerCBranchISN, long WorkerCISN, String CheckNumber,
            String CheckDueDate,long CheckBankBranchISN, long CheckBankISN, ArrayList<Long> ItemBranchISN, ArrayList<Long> ItemISN, ArrayList<Long> PriceTypeBranchISN,
            ArrayList<Long> PriceTypeISN, ArrayList<Long> StoreBranchISN, ArrayList<Long> StoreISN, ArrayList<Float> BasicQuantity, ArrayList<Float> BonusQuantity,
            ArrayList<Float> TotalQuantity, ArrayList<Double> Price, ArrayList<Long> MeasureUnitBranchISN, ArrayList<Long> MeasureUnitISN, ArrayList<Long> BasicMeasureUnitBranchISN,
            ArrayList<Long> BasicMeasureUnitISN, ArrayList<String> ItemSerial, ArrayList<String> ExpireDate, ArrayList<Long> ColorBranchISN, ArrayList<Long> ColorISN, ArrayList<Long> SizeBranchISN,
            ArrayList<Long> SizeISN, ArrayList<Long> SeasonBranchISN, ArrayList<Long> SeasonISN, ArrayList<Long> Group1BranchISN, ArrayList<Long> Group1ISN,
            ArrayList<Long> Group2BranchISN, ArrayList<Long> Group2ISN, ArrayList<String> LineNotes, Long numberOFItems, ArrayList<Double> NetPrice, ArrayList<Double> BasicMeasureUnitQuantity,
                             ArrayList<Boolean> ExpireDateBool, ArrayList<Boolean> ColorsBool, ArrayList<Boolean> SeasonsBool, ArrayList<Boolean> SizesBool, ArrayList<Boolean> SerialBool,
                             ArrayList<Boolean> Group1Bool, ArrayList<Boolean> Group2Bool, ArrayList<Boolean> ServiceItem, ArrayList<Double>ItemTax,ArrayList<Double>TaxValue, double TotalLinesTaxVal, int allowStoreMinus,
                             ArrayList<String> itemName, ArrayList<Double> discount1, Integer AllowStoreMinusConfirm, float latitude, float longitude, String invoiceType, Integer moveType, ArrayList<Long> StoreBranchISN2, ArrayList<Long> StoreISN2
    ){
        Log.e("checkInvoice" , "Triggered request");

        Observable<InvoiceResponse> invoiceResponse = apiClient.placeInvoice(BranchISN,uuid,CashType,SaleType,DealerType, DealerBranchISN,DealerISN,SalesManBranchISN,SalesManISN,
                HeaderNotes, TotalLinesValue, ServiceValue, ServicePer, DeliveryValue, TotalValueAfterServices, BasicDiscountVal, BasicDiscountPer, TotalValueAfterDisc,
                BasicTaxVal, BasicTaxPer, TotalValueAfterTax,NetValue,PaidValue,RemainValue, SafeDepositeBranchISN, SafeDepositeISN, BankBranchISN, BankISN, TableNumber,DeliveryPhone,DeliveryAddress,WorkerCBranchISN,
                WorkerCISN, CheckNumber,CheckDueDate,CheckBankBranchISN,CheckBankISN,ItemBranchISN,ItemISN,PriceTypeBranchISN,PriceTypeISN, StoreBranchISN,StoreISN,BasicQuantity,BonusQuantity,
                TotalQuantity,Price,MeasureUnitBranchISN,MeasureUnitISN,BasicMeasureUnitBranchISN,BasicMeasureUnitISN,ItemSerial,ExpireDate, ColorBranchISN,ColorISN,SizeBranchISN,
                SizeISN, SeasonBranchISN,SeasonISN,Group1BranchISN,Group1ISN,Group2BranchISN,Group2ISN,LineNotes,numberOFItems,2, NetPrice, BasicMeasureUnitQuantity, ExpireDateBool,
                ColorsBool, SeasonsBool,SizesBool, SerialBool, Group1Bool, Group2Bool, ServiceItem, ItemTax, TaxValue, TotalLinesTaxVal, allowStoreMinus,itemName, discount1,AllowStoreMinusConfirm,
                latitude,longitude, invoiceType, moveType, StoreBranchISN2, StoreISN2
                )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observer<InvoiceResponse> observer = new Observer<InvoiceResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull InvoiceResponse invoiceResponse) {
                responseDataMutableLiveData.setValue(invoiceResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("ERRORR !!", String.valueOf(e));
            }

            @Override
            public void onComplete() {

            }
        };
        invoiceResponse.subscribe(observer);
      }


      public void checkItem(String uuid ,ArrayList<Long> ItemBranchISN, ArrayList<Long> ItemISN, ArrayList<Long> PriceTypeBranchISN,
                            ArrayList<Long> PriceTypeISN, ArrayList<Long> StoreBranchISN, ArrayList<Long> StoreISN, ArrayList<Float> BasicQuantity, ArrayList<Float> BonusQuantity,
                            ArrayList<Float> TotalQuantity, ArrayList<Double> Price, ArrayList<Long> MeasureUnitBranchISN, ArrayList<Long> MeasureUnitISN, ArrayList<Long> BasicMeasureUnitBranchISN,
                            ArrayList<Long> BasicMeasureUnitISN, ArrayList<String> ItemSerial, ArrayList<String> ExpireDate, ArrayList<Long> ColorBranchISN, ArrayList<Long> ColorISN, ArrayList<Long> SizeBranchISN,
                            ArrayList<Long> SizeISN, ArrayList<Long> SeasonBranchISN, ArrayList<Long> SeasonISN, ArrayList<Long> Group1BranchISN, ArrayList<Long> Group1ISN,
                            ArrayList<Long> Group2BranchISN, ArrayList<Long> Group2ISN, ArrayList<String> LineNotes, ArrayList<Double> NetPrice, ArrayList<Double> BasicMeasureUnitQuantity,
                            ArrayList<Boolean> ExpireDateBool, ArrayList<Boolean> ColorsBool, ArrayList<Boolean> SeasonsBool, ArrayList<Boolean> SizesBool, ArrayList<Boolean> SerialBool,
                            ArrayList<Boolean> Group1Bool, ArrayList<Boolean> Group2Bool, ArrayList<Boolean> ServiceItem, ArrayList<Double>ItemTax,ArrayList<Double>TaxValue,
                            ArrayList<String> itemName, ArrayList<Double> discount1, int allowStoreMinus, Integer AllowStoreMinusConfirm){

          Observable<InvoiceResponse> checkItem = apiClient.checkItem(uuid, ItemBranchISN,ItemISN,PriceTypeBranchISN,PriceTypeISN, StoreBranchISN,StoreISN,BasicQuantity,BonusQuantity,
                  TotalQuantity,Price,MeasureUnitBranchISN,MeasureUnitISN,BasicMeasureUnitBranchISN,BasicMeasureUnitISN,ItemSerial,ExpireDate, ColorBranchISN,ColorISN,SizeBranchISN,
                  SizeISN, SeasonBranchISN,SeasonISN,Group1BranchISN,Group1ISN,Group2BranchISN,Group2ISN,LineNotes, NetPrice, BasicMeasureUnitQuantity, ExpireDateBool,
                  ColorsBool, SeasonsBool,SizesBool, SerialBool, Group1Bool, Group2Bool, ServiceItem, ItemTax, TaxValue, itemName, discount1,allowStoreMinus,AllowStoreMinusConfirm
          )
                  .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
          Observer<InvoiceResponse> observer = new Observer<InvoiceResponse>() {
              @Override
              public void onSubscribe(@NonNull Disposable d) {

              }

              @Override
              public void onNext(@NonNull InvoiceResponse invoiceResponse) {
                  checkItemMutableLiveData.setValue(invoiceResponse);
              }

              @Override
              public void onError(@NonNull Throwable e) {


                  Log.e("ERRORR !!", String.valueOf(e));
              }

              @Override
              public void onComplete() {

              }
          };
          checkItem.subscribe(observer);
      }


    public void getCustomerBalance(String uuid, String dealerISN, String branchISN, String dealerType, String dealerName) {
        Observable<CustomerBalance> customerObservable = apiClient.getCustomerBalance(uuid, dealerISN, branchISN, dealerType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observer<CustomerBalance> observer = new Observer<CustomerBalance>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull CustomerBalance customer) {
                customerBalanceLiveData.postValue(customer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        customerObservable.subscribe(observer);
    }

}
