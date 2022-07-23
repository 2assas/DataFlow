package com.example.dataflow.ViewModels;

import android.util.Log;
import android.widget.SearchView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dataflow.pojo.receipts.ReceiptModel;
import com.example.dataflow.pojo.receipts.ReceiptResponse;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReceiptsVM extends ViewModel {

    public MutableLiveData<ReceiptResponse> receiptResponseMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ReceiptModel> receiptModelMutableLiveData= new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);


    public void createReceipt(long BranchISN, String uuid, int CashType, int SaleType, int DealerType, int DealerBranchISN, long DealerISN, long SalesManBranchISN,
            long SalesManISN, String HeaderNotes, double TotalLinesValue, double ServiceValue, double ServicePer, double DeliveryValue,
            double TotalValueAfterServices,double BasicDiscountVal, double BasicDiscountPer, double TotalValueAfterDisc,double BasicTaxVal,
            double BasicTaxPer,double TotalValueAfterTax, double NetValue, double PaidValue, double RemainValue, long SafeDepositeBranchISN, long SafeDepositeISN, long BankBranchISN,
            long BankISN, String TableNumber, String DeliveryPhone, String DeliveryAddress, long WorkerCBranchISN, long WorkerCISN, String CheckNumber,
            String CheckDueDate,long CheckBankBranchISN, long CheckBankISN, int createSource, float latitude, float longitude){

        Observable<ReceiptResponse> receiptResponseObservable = apiClient.createReceipt(BranchISN,uuid,CashType,SaleType,DealerType, DealerBranchISN,DealerISN,SalesManBranchISN,SalesManISN,
                HeaderNotes, TotalLinesValue, ServiceValue, ServicePer, DeliveryValue, TotalValueAfterServices, BasicDiscountVal, BasicDiscountPer, TotalValueAfterDisc,
                BasicTaxVal, BasicTaxPer, TotalValueAfterTax,NetValue,PaidValue,RemainValue, SafeDepositeBranchISN, SafeDepositeISN, BankBranchISN, BankISN, TableNumber,DeliveryPhone,DeliveryAddress,WorkerCBranchISN,
                WorkerCISN, CheckNumber,CheckDueDate,CheckBankBranchISN,CheckBankISN, createSource, latitude, longitude).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());

        receiptResponseObservable.subscribe(new Observer<ReceiptResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ReceiptResponse receiptResponse) {
                receiptResponseMutableLiveData.postValue(receiptResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("ERROR Receipts", ""+e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getReceipt(long branchISN, String uuid, String moveId, long workerCBranchISN, long workerCISN, int permission){

        Observable<ReceiptModel> receiptModelObservable = apiClient.getReceipt(branchISN, uuid, moveId, workerCBranchISN,workerCISN,permission)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        receiptModelObservable.subscribe(new Observer<ReceiptModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull ReceiptModel receiptModel) {
                    receiptModelMutableLiveData.postValue(receiptModel);
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("ERROR get Receipts",""+e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
