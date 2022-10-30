package com.dataflowstores.dataflow.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.webService.ServiceGenerator;
import com.dataflowstores.dataflow.pojo.invoice.Invoice;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PrintInvoiceVM extends ViewModel {

    public MutableLiveData<Invoice> invoiceMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);


    public void getPrintingData(String branchISN, String uuid, String moveID, String workerCBranchISN, String workerCISN, Context context, Integer moveType){
        Observable<Invoice> invoiceObservable = apiClient.getPrintingData(branchISN,uuid, moveID, workerCBranchISN,workerCISN, App.currentUser.getPermission(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observer<Invoice> invoiceObserver = new Observer<Invoice>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Invoice invoice) {
                invoiceMutableLiveData.postValue(invoice);
                errorMutableLiveData.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                errorMutableLiveData.setValue(true);
                Log.e("checkError", e.toString() + " -- "+e.getMessage());
                new AlertDialog.Builder(context).setTitle("رقم فاتوره خطأ")
                        .setMessage("من فضلك تأكد من رقم الفاتورة")
                        .setPositiveButton("أغلاق", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onComplete() {
            }
        };
        invoiceObservable.subscribe(invoiceObserver);
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
