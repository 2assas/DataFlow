package com.dataflowstores.dataflow.ui.reports;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.financialReport.FinancialReportResponse;
import com.dataflowstores.dataflow.pojo.financialReport.ReportBody;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.report.StoreReportModel;
import com.dataflowstores.dataflow.pojo.report.WorkersResponse;
import com.dataflowstores.dataflow.pojo.report.itemSalesReport.ItemSalesResponse;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
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

public class ReportViewModel extends ViewModel {

    public MutableLiveData<StoreReportModel> storeReportModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Branches> branchesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<WorkersResponse> workersResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<SafeDeposit> safeDepositMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Banks> banksMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<FinancialReportResponse> financialReportResponseMutableLiveData = new MutableLiveData<>();
    public SingleLiveEvent<ItemSalesResponse> itemSalesResponseMutableLiveData = new SingleLiveEvent<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void getStoreReport(String uuid, Integer storeBranchISN, Integer storeISN, Integer itemBranchISN, Integer itemISN, String itemName) {
        Observable<StoreReportModel> storeReportModelObservable = apiClient.getStoresReport(uuid, storeBranchISN, storeISN, itemBranchISN, itemISN, null, itemName, 0,
                        App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()))
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
            public void onError(@NonNull Throwable throwable) {
                Log.e("error ", throwable.toString());
                if (throwable instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
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
            public void onError(@NonNull Throwable throwable) {
                Log.e("ErrorGetBranches", throwable.toString());
                if (throwable instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getWorkers(String uuid){
        Observable<WorkersResponse> observable = apiClient.getWorkers(uuid, App.currentUser.getBranchISN(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission(), 0).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<WorkersResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull WorkersResponse workersResponse) {
                workersResponseMutableLiveData.postValue(workersResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("ErrorGetBranches", throwable.toString());
                if (throwable instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getSafeDeposit(long branchISN, String uuid, int moveType){
        Observable<SafeDeposit> getSafeDeposit = apiClient.getSafeDeposit(branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getSafeDepositBranchISN(), App.currentUser.getSafeDepositISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getSafeDeposit.subscribe(safeDeposit -> {
            safeDepositMutableLiveData.setValue(safeDeposit);
        }, throwable -> {
            if (throwable instanceof HttpException) {
                ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                try {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }
    public void getBanks(long branchISN, String uuid) {
        Observable<Banks> getBanks = apiClient.getBanks(branchISN, App.currentUser.getPermission(), uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBanks.subscribe(banks -> {
            banksMutableLiveData.setValue(banks);
        }, throwable -> {
            if (throwable instanceof HttpException) {
                ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                try {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    public void getFinancialReport(ReportBody reportBody,String uuid, long storeBranchISN, long storeISN, long workerBranch){
        Observable<FinancialReportResponse> observable = apiClient.getFinancialReport(reportBody,uuid, storeBranchISN,storeISN, workerBranch,App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(),App.currentUser.getWorkStationName(),String.valueOf( App.currentUser.getWorkStationISN()),String.valueOf( App.currentUser.getWorkerBranchISN()))
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
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERRORpostReport", throwable.toString());
                if (throwable instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getItemSalesReport(String uuid, long branchISN, String fromWorkday, String toWorkday, String shiftISN,
                                   long workerBranchISN, String workerISN, String from, String to, Long vendorId) {
        Observable<ItemSalesResponse> observable = apiClient.getItemSalesReport(uuid, branchISN, fromWorkday, toWorkday,
                        shiftISN, workerBranchISN, workerISN, from, to, vendorId)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<ItemSalesResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ItemSalesResponse itemSalesResponse) {
                itemSalesResponseMutableLiveData.postValue(itemSalesResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERRORpostReport", throwable.toString());
                if (throwable instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) throwable).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(throwable.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
