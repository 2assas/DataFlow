package com.dataflowstores.dataflow.ViewModels;

import static com.dataflowstores.dataflow.App.selectedFoundation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.webService.ServiceGenerator;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptModel;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptResponse;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;

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

public class ReceiptsVM extends ViewModel {

    public MutableLiveData<ReceiptResponse> receiptResponseMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ReceiptModel> receiptModelMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();

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
                WorkerCISN, CheckNumber,CheckDueDate,CheckBankBranchISN,CheckBankISN, createSource, latitude, longitude, App.currentUser.getWorkerName(),
                App.currentUser.getUserName(),App.currentUser.getWorkStationName(),String.valueOf( App.currentUser.getWorkStationISN()),String.valueOf( App.currentUser.getWorkerBranchISN()),selectedFoundation,
                App.currentUser.getLogIn_BISN(),
                App.currentUser.getLogIn_UID(),
                App.currentUser.getLogIn_WBISN(),
                App.currentUser.getLogIn_WISN(),
                App.currentUser.getLogIn_WName(),
                App.currentUser.getLogIn_WSBISN(),
                App.currentUser.getLogIn_WSISN(),
                App.currentUser.getLogIn_WSName(),
                App.currentUser.getLogIn_CS(),
                App.currentUser.getLogIn_VN(),
                App.currentUser.getLogIn_FAlternative()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());

        receiptResponseObservable.subscribe(new Observer<ReceiptResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ReceiptResponse receiptResponse) {
                receiptResponseMutableLiveData.postValue(receiptResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
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
                Log.e("ERROR Receipts", ""+throwable);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getReceipt(long branchISN, String uuid, String moveId, long workerCBranchISN, long workerCISN, int permission){

        Observable<ReceiptModel> receiptModelObservable = apiClient.getReceipt(branchISN, uuid, moveId, workerCBranchISN,workerCISN,permission,selectedFoundation,
                        App.currentUser.getLogIn_BISN(),
                        App.currentUser.getLogIn_UID(),
                        App.currentUser.getLogIn_WBISN(),
                        App.currentUser.getLogIn_WISN(),
                        App.currentUser.getLogIn_WName(),
                        App.currentUser.getLogIn_WSBISN(),
                        App.currentUser.getLogIn_WSISN(),
                        App.currentUser.getLogIn_WSName(),
                        App.currentUser.getLogIn_CS(),
                        App.currentUser.getLogIn_VN(),
                        App.currentUser.getLogIn_FAlternative())
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
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERROR get Receipts",""+throwable);
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
        });
    }
    public void getCustomerBalance(String uuid, String dealerISN, String branchISN, String dealerType, String dealerName) {
        Observable<CustomerBalance> customerObservable = apiClient.getCustomerBalance(uuid, dealerISN, branchISN, dealerType,selectedFoundation,
                App.currentUser.getLogIn_BISN(),
                App.currentUser.getLogIn_UID(),
                App.currentUser.getLogIn_WBISN(),
                App.currentUser.getLogIn_WISN(),
                App.currentUser.getLogIn_WName(),
                App.currentUser.getLogIn_WSBISN(),
                App.currentUser.getLogIn_WSISN(),
                App.currentUser.getLogIn_WSName(),
                App.currentUser.getLogIn_CS(),
                App.currentUser.getLogIn_VN(),
                App.currentUser.getLogIn_FAlternative()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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
        customerObservable.subscribe(observer);
    }

}
