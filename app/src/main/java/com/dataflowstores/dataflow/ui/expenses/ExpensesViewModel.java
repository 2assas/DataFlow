package com.dataflowstores.dataflow.ui.expenses;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.expenses.MainExpResponse;
import com.dataflowstores.dataflow.pojo.expenses.SubExpResponse;
import com.dataflowstores.dataflow.pojo.expenses.WorkerResponse;
import com.dataflowstores.dataflow.pojo.expenses.AllExpensesResponse;
import com.dataflowstores.dataflow.pojo.expenses.ExpensesResponse;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptResponse;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExpensesViewModel extends ViewModel {

    public MutableLiveData<AllExpensesResponse> allExpResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ExpensesResponse> expensesModelMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ReceiptResponse> expensesResponseMutableLiveData= new MutableLiveData<>();

    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();

    ApiClient tokenService = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void SelectBranchStaff(String uuid) {
        Observable<MainExpResponse> getMainExp = tokenService.getMainExpenses(uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<SubExpResponse> getSubExp = tokenService.getSubExpenses(uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<WorkerResponse> getWorkers = tokenService.getExpWorkers(uuid, App.currentUser.getBranchISN(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission(), 11).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

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
            public void onError(@NonNull Throwable e) {
                Log.e("checkError", e.toString());
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

        Observable<ReceiptResponse> receiptResponseObservable = tokenService.createExpense(BranchISN,uuid,CashType,SaleType,
                HeaderNotes, TotalLinesValue, ServiceValue, ServicePer, DeliveryValue, TotalValueAfterServices, BasicDiscountVal, BasicDiscountPer, TotalValueAfterDisc,
                BasicTaxVal, BasicTaxPer, TotalValueAfterTax,NetValue,PaidValue,RemainValue, SafeDepositeBranchISN, SafeDepositeISN, BankBranchISN, BankISN, TableNumber,DeliveryPhone,DeliveryAddress,WorkerCBranchISN,
                WorkerCISN, CheckNumber,CheckDueDate,CheckBankBranchISN,CheckBankISN, createSource, latitude, longitude,  ShiftISN,  MainExpMenuISN,
                MainExpMenuBranchISN,  MainExpMenuName,  SubExpMenuISN,  SubExpMenuBranchISN,  SubExpMenuName,  SelectedWorkerBranchISN, SelectedWorkerISN,App.currentUser.getWorkerName(),
                App.currentUser.getUserName(),App.currentUser.getWorkStationName(),String.valueOf( App.currentUser.getWorkStationISN()),String.valueOf( App.currentUser.getWorkerBranchISN())
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
            public void onError(@NonNull Throwable e) {
                Log.e("ERROR Receipts", ""+e);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void getExpenses(long branchISN, String uuid, String moveId, long workerCBranchISN, long workerCISN, int permission){

        Observable<ExpensesResponse> receiptModelObservable = tokenService.getExpenses(branchISN, uuid, moveId, workerCBranchISN,workerCISN,permission)
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
            public void onError(@NonNull Throwable e) {
                Log.e("ERROR get Receipts",""+e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getCustomerBalance(String uuid, String dealerISN, String branchISN, String dealerType, String dealerName) {
        Observable<CustomerBalance> customerObservable = tokenService.getCustomerBalance(uuid, dealerISN, branchISN, dealerType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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