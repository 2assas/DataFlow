package com.dataflowstores.dataflow.ViewModels;

import static com.dataflowstores.dataflow.App.currentUser;
import static com.dataflowstores.dataflow.App.selectedFoundation;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnSales;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Sales;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.GeneralRequestBodyUtil;
import com.dataflowstores.dataflow.pojo.invoice.InitialAPIs;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.PriceType;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class SettingVM extends ViewModel {
    public MutableLiveData<Stores> storesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> allDone = new MutableLiveData<>();
    public MutableLiveData<InitialAPIs> initialAPIsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Branches> branchesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();
    Map<String, String> generalParams = GeneralRequestBodyUtil.toQueryParams();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    @SuppressLint("CheckResult")
    public void getStoresCashing(long branchISN, String uuid, int moveType){
        Observable<Stores> getStores = apiClient.getStores(generalParams, branchISN, // Specific parameter
                                                           uuid,      // Specific parameter
                                                           App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            getStores.subscribe(stores -> {
                storesMutableLiveData.setValue(stores);
            },throwable -> {
                if (throwable instanceof IOException) {
                    //handle network error
                    toastErrorMutableLiveData.postValue("No Internet Connection!");
                } else if (throwable instanceof HttpException) {
                    ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    //handle HTTP error response code
                } else {
                    //handle other exceptions
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
            });
    }

    @SuppressLint("CheckResult")
    public void getInitialInvoiceApis(long branchISN, String uuid, int moveType) {
        Observable<Banks> getBanks =
                apiClient.getBanks(generalParams, uuid, branchISN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<Stores> getStores = apiClient.getStores(generalParams, branchISN, // Specific parameter
                                                           uuid,      // Specific parameter
                                                           App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<SafeDeposit> getSafeDeposits = apiClient.getSafeDeposit(generalParams, branchISN, uuid, App.currentUser.getSafeDepositBranchISN(), App.currentUser.getSafeDepositISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<PriceType> getPriceTypes =
                apiClient.getPriceType(generalParams, uuid, currentUser.getPricesTypeBranchISN(), currentUser.getPricesTypeISN(), null
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observable<InitialAPIs> zipper = Observable.zip(getStores, getBanks, getSafeDeposits, getPriceTypes, InitialAPIs::new);

        zipper.subscribe(initialAPIs -> {
            initialAPIsMutableLiveData.postValue(initialAPIs);
            App.allPriceType= initialAPIs.getPriceType().getData();
            allDone.postValue(true);
        },throwable -> {
            allDone.postValue(false);
            if (throwable instanceof IOException) {
                //handle network error
                toastErrorMutableLiveData.postValue("No Internet Connection!");
            } else if (throwable instanceof HttpException) {
                ResponseBody errorBody = Objects.requireNonNull(((HttpException) throwable).response()).errorBody();
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                //handle HTTP error response code
            } else {
                //handle other exceptions
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }
    public void getBranches(String uuid) {
        Observable<Branches> getBranches = apiClient.getBranches(generalParams, uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observer<Branches> observer = new Observer<Branches>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Branches branches) {
                branchesMutableLiveData.postValue(branches);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("branchesError", throwable.toString());
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
            }

            @Override
            public void onComplete() {

            }
        };
        getBranches.subscribe(observer);
    }

}
