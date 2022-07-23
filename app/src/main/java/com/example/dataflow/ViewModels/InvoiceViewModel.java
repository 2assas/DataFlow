package com.example.dataflow.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dataflow.pojo.users.Customer;
import com.example.dataflow.pojo.users.CustomerBalance;
import com.example.dataflow.pojo.users.SalesMan;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InvoiceViewModel extends ViewModel {
    public MutableLiveData<Customer> customerLiveData = new MutableLiveData<>();
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<SalesMan> salesManLiveData = new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void getCustomer(String uuid, String customerName, Long WorkerBranchISN, Long WorkerISN) {
        Observable<Customer> customerObservable = apiClient.getCustomer(customerName, uuid, WorkerBranchISN, WorkerISN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        customerObservable.subscribe(customer -> {
            customerLiveData.setValue(customer);
        });
    }


    public void getSalesMan(String uuid, String salesManName, Long WorkerBranchISN, Long WorkerISN) {
        Observable<SalesMan> salesManObservable = apiClient.getSalesMan(salesManName, uuid, WorkerBranchISN, WorkerISN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        salesManObservable.subscribe(salesMan -> {
            salesManLiveData.setValue(salesMan);
        });
    }

    public void getCustomerBalance(String uuid, String dealerISN, String branchISN, String dealerType, String dealerName) {
        Observable<CustomerBalance> customerObservable = apiClient.getCustomerBalance(uuid, dealerISN, branchISN, dealerType, dealerName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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
