package com.example.dataflow.ui.reports;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dataflow.App;
import com.example.dataflow.pojo.financialReport.FinancialReportResponse;
import com.example.dataflow.pojo.financialReport.ReportBody;
import com.example.dataflow.pojo.report.Branches;
import com.example.dataflow.pojo.report.StoreReportModel;
import com.example.dataflow.pojo.report.WorkersResponse;
import com.example.dataflow.pojo.settings.Banks;
import com.example.dataflow.pojo.settings.SafeDeposit;
import com.example.dataflow.pojo.workStation.Branch;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReportViewModel extends ViewModel {

    public MutableLiveData<StoreReportModel> storeReportModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Branches> branchesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<WorkersResponse> workersResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<SafeDeposit> safeDepositMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Banks> banksMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<FinancialReportResponse> financialReportResponseMutableLiveData = new MutableLiveData<>();
    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void getStoreReport(String uuid, int storeBranchISN, int storeISN){
        Observable<StoreReportModel> storeReportModelObservable= apiClient.getStoresReport(uuid, storeBranchISN, storeISN)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        storeReportModelObservable.subscribe(new Observer<StoreReportModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StoreReportModel storeReportModel) {
                storeReportModelMutableLiveData.setValue(storeReportModel);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("error ", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getBranches(String uuid){
        Observable<Branches> observable = apiClient.getBranches(uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Branches>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Branches branch) {
                branchesMutableLiveData.postValue(branch);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("ErrorGetBranches", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getWorkers(String uuid){
        Observable<WorkersResponse> observable = apiClient.getWorkers(uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<WorkersResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull WorkersResponse workersResponse) {
                workersResponseMutableLiveData.postValue(workersResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("ErrorGetBranches", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getSafeDeposit(long branchISN, String uuid){
        Observable<SafeDeposit> getSafeDeposit = apiClient.getSafeDeposit(branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getSafeDepositBranchISN(), App.currentUser.getSafeDepositISN()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getSafeDeposit.subscribe(safeDeposit -> {
            safeDepositMutableLiveData.setValue(safeDeposit);
        });
    }
    public void getBanks(long branchISN, String uuid){
        Observable<Banks> getBanks = apiClient.getBanks(branchISN, App.currentUser.getPermission(),uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBanks.subscribe(banks -> {banksMutableLiveData.setValue(banks);});
    }

    public void getFinancialReport(ReportBody reportBody,String uuid, long storeBranchISN, long storeISN, long workerBranch){
        Observable<FinancialReportResponse> observable = apiClient.getFinancialReport(reportBody,uuid, storeBranchISN,storeISN, workerBranch)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<FinancialReportResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull FinancialReportResponse financialReportResponse) {
                financialReportResponseMutableLiveData.postValue(financialReportResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("ERRORpostReport", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
