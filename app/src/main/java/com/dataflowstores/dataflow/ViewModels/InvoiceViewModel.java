package com.dataflowstores.dataflow.ViewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.webService.ServiceGenerator;
import com.dataflowstores.dataflow.pojo.users.Customer;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.pojo.users.SalesMan;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class InvoiceViewModel extends ViewModel {
    public MutableLiveData<Customer> customerLiveData = new MutableLiveData<>();
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<SalesMan> salesManLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void getCustomer(String uuid, String customerName, Long WorkerBranchISN, Long WorkerISN) {
        Observable<Customer> customerObservable = apiClient.getCustomer(customerName, uuid, WorkerBranchISN, WorkerISN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        customerObservable.subscribe(customer -> {
            customerLiveData.setValue(customer);
        }, throwable -> {
            if (throwable instanceof HttpException) {
                ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
            }else{
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }


    public void getSalesMan(String uuid, String salesManName, Long WorkerBranchISN, Long WorkerISN) {
        Observable<SalesMan> salesManObservable = apiClient.getSalesMan(salesManName, uuid, WorkerBranchISN, WorkerISN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        salesManObservable.subscribe(salesMan -> {
            salesManLiveData.setValue(salesMan);
        }, throwable -> {
            throwable.printStackTrace();
            if (throwable instanceof HttpException) {
                ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
            }else{
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
        });
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
            public void onError(@NonNull Throwable throwable) {
                if (throwable instanceof HttpException) {
                    ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        };
        customerObservable.subscribe(observer);
    }


}
