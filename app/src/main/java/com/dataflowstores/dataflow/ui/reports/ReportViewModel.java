package com.dataflowstores.dataflow.ui.reports;

import static com.dataflowstores.dataflow.App.selectedFoundation;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.financialReport.FinancialReportResponse;
import com.dataflowstores.dataflow.pojo.financialReport.ReportBody;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.report.DataItem;
import com.dataflowstores.dataflow.pojo.report.StoreReportModel;
import com.dataflowstores.dataflow.pojo.report.WorkersResponse;
import com.dataflowstores.dataflow.pojo.report.cashierMoves.CashierMovesReportResponse;
import com.dataflowstores.dataflow.pojo.report.cashierMoves.moveTypes.MoveTypesResponse;
import com.dataflowstores.dataflow.pojo.report.itemSalesReport.ItemSalesResponse;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
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
    public MutableLiveData<CashierMovesReportResponse> cashierMovesReportResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MoveTypesResponse> moveTypesResponseMutableLiveData = new MutableLiveData<>();
    public SingleLiveEvent<ItemSalesResponse> itemSalesResponseMutableLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<ItemSalesResponse> supplierSalesResponseMutableLiveData = new SingleLiveEvent<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);

    public void getStoreReport(String uuid, Integer storeBranchISN, Integer storeISN, Integer itemBranchISN, Integer itemISN, String itemName) {
        Observable<StoreReportModel> storeReportModelObservable = apiClient.getStoresReport(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid, storeBranchISN, storeISN, itemBranchISN, itemISN, null, itemName, 0,
                        App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()),selectedFoundation,
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
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6())
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

    public void getBranches(String uuid) {
        Observable<Branches> observable = apiClient.getBranches(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid,selectedFoundation,
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
                ,App.currentUser.getMobileSalesMaxDiscPer()
                ,App.currentUser.getShiftSystemActivate()
                ,App.currentUser.getLogIn_ShiftBranchISN()
                ,App.currentUser.getLogIn_ShiftISN()
                ,App.currentUser.getLogIn_Spare1()
                ,App.currentUser.getLogIn_Spare2()
                ,App.currentUser.getLogIn_Spare3()
                ,App.currentUser.getLogIn_Spare4()
                ,App.currentUser.getLogIn_Spare5()
                ,App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

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

    public void getWorkers(String uuid) {
        Observable<WorkersResponse> observable = apiClient.getWorkers(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid, App.currentUser.getBranchISN(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission(), 0,selectedFoundation,
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
                ,App.currentUser.getMobileSalesMaxDiscPer()
                ,App.currentUser.getShiftSystemActivate()
                ,App.currentUser.getLogIn_ShiftBranchISN()
                ,App.currentUser.getLogIn_ShiftISN()
                ,App.currentUser.getLogIn_Spare1()
                ,App.currentUser.getLogIn_Spare2()
                ,App.currentUser.getLogIn_Spare3()
                ,App.currentUser.getLogIn_Spare4()
                ,App.currentUser.getLogIn_Spare5()
                ,App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

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

    public void getSafeDeposit(long branchISN, String uuid, int moveType) {
        Observable<SafeDeposit> getSafeDeposit = apiClient.getSafeDeposit(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getSafeDepositBranchISN(), App.currentUser.getSafeDepositISN(), App.currentUser.getAllBranchesWorker(), moveType,selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()                ,App.currentUser.getMobileSalesMaxDiscPer()
                ,App.currentUser.getShiftSystemActivate()
                ,App.currentUser.getLogIn_ShiftBranchISN()
                ,App.currentUser.getLogIn_ShiftISN()
                ,App.currentUser.getLogIn_Spare1()
                ,App.currentUser.getLogIn_Spare2()
                ,App.currentUser.getLogIn_Spare3()
                ,App.currentUser.getLogIn_Spare4()
                ,App.currentUser.getLogIn_Spare5()
                ,App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getSafeDeposit.subscribe(safeDeposit -> {
            safeDepositMutableLiveData.setValue(safeDeposit);
        }, throwable -> {
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

    public void getBanks(long branchISN, String uuid) {
        Observable<Banks> getBanks = apiClient.getBanks(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),branchISN, App.currentUser.getPermission(), uuid,selectedFoundation,
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
                App.currentUser.getLogIn_FAlternative()                ,App.currentUser.getMobileSalesMaxDiscPer()
                ,App.currentUser.getShiftSystemActivate()
                ,App.currentUser.getLogIn_ShiftBranchISN()
                ,App.currentUser.getLogIn_ShiftISN()
                ,App.currentUser.getLogIn_Spare1()
                ,App.currentUser.getLogIn_Spare2()
                ,App.currentUser.getLogIn_Spare3()
                ,App.currentUser.getLogIn_Spare4()
                ,App.currentUser.getLogIn_Spare5()
                ,App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBanks.subscribe(banks -> {
            banksMutableLiveData.setValue(banks);
        }, throwable -> {
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

    public void getFinancialReport(ReportBody reportBody, String uuid, long storeBranchISN, long storeISN, long workerBranch, String workerCISN, String workerCBranchISN) {
        Observable<FinancialReportResponse> observable = apiClient.getFinancialReport(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),reportBody, uuid, storeBranchISN, storeISN, workerBranch, App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()), workerCISN, workerCBranchISN,selectedFoundation,
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
                        App.currentUser.getLogIn_FAlternative()                ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6())
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

    public void getCashierMoves(ReportBody reportBody, String uuid, long storeBranchISN, long storeISN, long workerBranch, Integer moveType, CustomerData customerData, DataItem selectedWorker, CustomerData supplierData) {

        Observable<CashierMovesReportResponse> observable = apiClient.getCashierMovesReport(reportBody,App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(), uuid, storeBranchISN, storeISN, workerBranch, App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()),
                        moveType, customerData == null ? null : customerData.getDealerType(), customerData == null ? null : customerData.getBranchISN(), customerData == null ? null : customerData.getDealer_ISN(),
                        selectedWorker == null ? null : selectedWorker.getWorkerISN(), selectedWorker == null ? null : Long.valueOf(selectedWorker.getBranchISN()),selectedFoundation,
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
                        App.currentUser.getLogIn_FAlternative(),
                        supplierData==null? null: supplierData.getDealerType(),
                        supplierData==null? null: supplierData.getBranchISN(),
                        supplierData==null? null: supplierData.getDealer_ISN()
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6()
                        )
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<CashierMovesReportResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull CashierMovesReportResponse cashierMovesReportResponse) {
                cashierMovesReportResponseMutableLiveData.postValue(cashierMovesReportResponse);
            }
            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERRORpostReport", throwable.toString());
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

    @SuppressLint("CheckResult")
    public void getMoveTypes(String uuid) {
        apiClient.getMoveTypes(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid,selectedFoundation,
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
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(moveTypesResponse -> {
                    moveTypesResponseMutableLiveData.postValue(moveTypesResponse);
                }, throwable -> {
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

    public void getItemSalesReport(String uuid, long branchISN, String fromWorkday, String toWorkday, String shiftISN,
                                   long workerBranchISN, String workerISN, String from, String to, Long vendorId, String workerCISN, String workerCBranchISN, Integer dealerType, Long dealerBranchISN, Long dealer_ISN) {

        Observable<ItemSalesResponse> observable = apiClient.getItemSalesReport(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid, branchISN, fromWorkday, toWorkday,
                        shiftISN, workerBranchISN, workerISN, from, to, vendorId, workerCISN, workerCBranchISN, App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()), dealerType, dealerBranchISN, dealer_ISN,selectedFoundation,
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
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6())
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


    public void getSupplierSalesReport(String uuid, long branchISN, String fromWorkday, String toWorkday, String shiftISN,
                                   long workerBranchISN, String workerISN, String from, String to, Long vendorId, String workerCISN, String workerCBranchISN, Integer dealerType, Long dealerBranchISN, Long dealer_ISN) {

        Observable<ItemSalesResponse> observable = apiClient.getSupplierSalesReport(App.currentUser.getIllustrativeQuantity(),App.currentUser.getDeviceID(), App.currentUser.getLogIn_CurrentWorkingDayDate(),App.currentUser.getVendorID(),uuid, branchISN, fromWorkday, toWorkday,
                        shiftISN, workerBranchISN, workerISN, from, to, vendorId, workerCISN, workerCBranchISN, App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(), App.currentUser.getWorkStationName(), String.valueOf(App.currentUser.getWorkStationISN()), String.valueOf(App.currentUser.getWorkerBranchISN()), dealerType, dealerBranchISN, dealer_ISN,selectedFoundation,
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
                        ,App.currentUser.getMobileSalesMaxDiscPer()
                        ,App.currentUser.getShiftSystemActivate()
                        ,App.currentUser.getLogIn_ShiftBranchISN()
                        ,App.currentUser.getLogIn_ShiftISN()
                        ,App.currentUser.getLogIn_Spare1()
                        ,App.currentUser.getLogIn_Spare2()
                        ,App.currentUser.getLogIn_Spare3()
                        ,App.currentUser.getLogIn_Spare4()
                        ,App.currentUser.getLogIn_Spare5()
                        ,App.currentUser.getLogIn_Spare6())
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<ItemSalesResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ItemSalesResponse itemSalesResponse) {
                supplierSalesResponseMutableLiveData.postValue(itemSalesResponse);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("ERRORpostReport", throwable.toString());
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


}
