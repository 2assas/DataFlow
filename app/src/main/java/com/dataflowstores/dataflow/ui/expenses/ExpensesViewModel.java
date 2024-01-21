package com.dataflowstores.dataflow.ui.expenses;

import static com.dataflowstores.dataflow.App.selectedFoundation;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.expenses.AllExpensesResponse;
import com.dataflowstores.dataflow.pojo.expenses.ExpensesResponse;
import com.dataflowstores.dataflow.pojo.expenses.MainExpResponse;
import com.dataflowstores.dataflow.pojo.expenses.SubExpResponse;
import com.dataflowstores.dataflow.pojo.expenses.WorkerResponse;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptResponse;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.utils.SingleLiveEvent;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

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

public class ExpensesViewModel extends ViewModel {

    public SingleLiveEvent<AllExpensesResponse> allExpResponseMutableLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<ExpensesResponse> expensesModelMutableLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<ReceiptResponse> expensesResponseMutableLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<String> toastErrorMutableLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<CustomerBalance> customerBalanceLiveData = new SingleLiveEvent<>();

    ApiClient tokenService = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void SelectBranchStaff(String uuid) {
        Observable<MainExpResponse> getMainExp = tokenService.getMainExpenses(uuid, selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()
                , App.currentUser.getMobileSalesMaxDiscPer()
                , App.currentUser.getShiftSystemActivate()
                , App.currentUser.getLogIn_ShiftBranchISN()
                , App.currentUser.getLogIn_ShiftISN()
                , App.currentUser.getLogIn_Spare1()
                , App.currentUser.getLogIn_Spare2()
                , App.currentUser.getLogIn_Spare3()
                , App.currentUser.getLogIn_Spare4()
                , App.currentUser.getLogIn_Spare5()
                , App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<SubExpResponse> getSubExp = tokenService.getSubExpenses(uuid, selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()
                , App.currentUser.getMobileSalesMaxDiscPer()
                , App.currentUser.getShiftSystemActivate()
                , App.currentUser.getLogIn_ShiftBranchISN()
                , App.currentUser.getLogIn_ShiftISN()
                , App.currentUser.getLogIn_Spare1()
                , App.currentUser.getLogIn_Spare2()
                , App.currentUser.getLogIn_Spare3()
                , App.currentUser.getLogIn_Spare4()
                , App.currentUser.getLogIn_Spare5()
                , App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<WorkerResponse> getWorkers = tokenService.getExpWorkers(uuid, App.currentUser.getBranchISN(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission(), 11, selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()
                , App.currentUser.getMobileSalesMaxDiscPer()
                , App.currentUser.getShiftSystemActivate()
                , App.currentUser.getLogIn_ShiftBranchISN()
                , App.currentUser.getLogIn_ShiftISN()
                , App.currentUser.getLogIn_Spare1()
                , App.currentUser.getLogIn_Spare2()
                , App.currentUser.getLogIn_Spare3()
                , App.currentUser.getLogIn_Spare4()
                , App.currentUser.getLogIn_Spare5()
                , App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observable<AllExpensesResponse> zipper = Observable.zip(getMainExp, getSubExp, getWorkers, AllExpensesResponse::new);

        zipper.subscribe(new Observer<AllExpensesResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull AllExpensesResponse allExpensesResponse) {
                allExpResponseMutableLiveData.postValue(allExpensesResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("checkError", throwable.toString());
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
    public void createExpenses(long BranchISN, String uuid, int CashType, int SaleType,
                              String HeaderNotes, double TotalLinesValue, double ServiceValue, double ServicePer, double DeliveryValue,
                              double TotalValueAfterServices,double BasicDiscountVal, double BasicDiscountPer, double TotalValueAfterDisc,double BasicTaxVal,
                              double BasicTaxPer,double TotalValueAfterTax, double NetValue, double PaidValue, double RemainValue, long SafeDepositeBranchISN, long SafeDepositeISN, long BankBranchISN,
                              long BankISN, String TableNumber, String DeliveryPhone, String DeliveryAddress, long WorkerCBranchISN, long WorkerCISN, String CheckNumber,
                              String CheckDueDate,long CheckBankBranchISN, long CheckBankISN, int createSource, float latitude, float longitude, Long ShiftISN, Long MainExpMenuISN
    ,Long MainExpMenuBranchISN, String MainExpMenuName, Long SubExpMenuISN, Long SubExpMenuBranchISN, String SubExpMenuName, Long SelectedWorkerBranchISN, Long SelectedWorkerISN){

        Observable<ReceiptResponse> receiptResponseObservable = tokenService.createExpense(BranchISN, uuid, CashType, SaleType,
                HeaderNotes, TotalLinesValue, ServiceValue, ServicePer, DeliveryValue, TotalValueAfterServices, BasicDiscountVal, BasicDiscountPer, TotalValueAfterDisc,
                BasicTaxVal, BasicTaxPer, TotalValueAfterTax, NetValue, PaidValue, RemainValue, SafeDepositeBranchISN, SafeDepositeISN, BankBranchISN, BankISN, TableNumber, DeliveryPhone, DeliveryAddress, WorkerCBranchISN,
                WorkerCISN, CheckNumber, CheckDueDate, CheckBankBranchISN, CheckBankISN, createSource, latitude, longitude, ShiftISN, MainExpMenuISN,
                MainExpMenuBranchISN, MainExpMenuName, SubExpMenuISN, SubExpMenuBranchISN, SubExpMenuName, SelectedWorkerBranchISN, SelectedWorkerISN, App.currentUser.getWorkerName(),
                App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()), selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()
                , App.currentUser.getMobileSalesMaxDiscPer()
                , App.currentUser.getShiftSystemActivate()
                , App.currentUser.getLogIn_ShiftBranchISN()
                , App.currentUser.getLogIn_ShiftISN()
                , App.currentUser.getLogIn_Spare1()
                , App.currentUser.getLogIn_Spare2()
                , App.currentUser.getLogIn_Spare3()
                , App.currentUser.getLogIn_Spare4()
                , App.currentUser.getLogIn_Spare5()
                , App.currentUser.getLogIn_Spare6()
        ).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());

        receiptResponseObservable.subscribe(new Observer<ReceiptResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ReceiptResponse expensesResponse) {
                expensesResponseMutableLiveData.postValue(expensesResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERROR Receipts", "" + throwable);
                Log.e("error ", throwable.toString());
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


    public void getExpenses(long branchISN, String uuid, String moveId, long workerCBranchISN, long workerCISN, int permission) {

        Observable<ExpensesResponse> receiptModelObservable = tokenService.getExpenses(branchISN, uuid, moveId, workerCBranchISN, workerCISN, permission, 11, selectedFoundation,
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
                        App.currentUser.getLogIn_FAlternative()
                        , App.currentUser.getMobileSalesMaxDiscPer()
                        , App.currentUser.getShiftSystemActivate()
                        , App.currentUser.getLogIn_ShiftBranchISN()
                        , App.currentUser.getLogIn_ShiftISN()
                        , App.currentUser.getLogIn_Spare1()
                        , App.currentUser.getLogIn_Spare2()
                        , App.currentUser.getLogIn_Spare3()
                        , App.currentUser.getLogIn_Spare4()
                        , App.currentUser.getLogIn_Spare5()
                        , App.currentUser.getLogIn_Spare6())
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        receiptModelObservable.subscribe(new Observer<ExpensesResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ExpensesResponse expensesResponse) {
                expensesModelMutableLiveData.postValue(expensesResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERROR get Receipts", "" + throwable);
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
        Observable<CustomerBalance> customerObservable = tokenService.getCustomerBalance(uuid, dealerISN, branchISN, dealerType, selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()
                , null, null, null, null, null, null, App.currentUser.getInvoiceCurrentBalanceTimeInInvoice()
                , App.currentUser.getMobileSalesMaxDiscPer()
                , App.currentUser.getShiftSystemActivate()
                , App.currentUser.getLogIn_ShiftBranchISN()
                , App.currentUser.getLogIn_ShiftISN()
                , App.currentUser.getLogIn_Spare1()
                , App.currentUser.getLogIn_Spare2()
                , App.currentUser.getLogIn_Spare3()
                , App.currentUser.getLogIn_Spare4()
                , App.currentUser.getLogIn_Spare5()
                , App.currentUser.getLogIn_Spare6()
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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