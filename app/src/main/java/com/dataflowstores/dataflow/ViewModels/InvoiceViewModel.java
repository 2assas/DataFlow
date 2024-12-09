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
import com.dataflowstores.dataflow.pojo.GeneralRequestBody;
import com.dataflowstores.dataflow.pojo.GeneralRequestBodyUtil;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.report.dealersBalancesReport.DealerCategoriesResponse;
import com.dataflowstores.dataflow.pojo.report.dealersBalancesReport.DealersBalancesResponse;
import com.dataflowstores.dataflow.pojo.settings.PriceType;
import com.dataflowstores.dataflow.pojo.settings.PriceTypeData;
import com.dataflowstores.dataflow.pojo.users.Customer;
import com.dataflowstores.dataflow.pojo.users.CustomerBalance;
import com.dataflowstores.dataflow.pojo.users.SalesMan;
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

public class InvoiceViewModel extends ViewModel {
    public MutableLiveData<Customer> customerLiveData = new MutableLiveData<>();
    public MutableLiveData<CustomerBalance> customerBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<SalesMan> salesManLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Branches> branchesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PriceTypeData> priceTypeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<DealerCategoriesResponse> dealerCategoriesResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<DealersBalancesResponse> dealersBalancesReportMutableLiveData = new MutableLiveData<>();
    Map<String, String> queryParams = GeneralRequestBodyUtil.toQueryParams();


    ApiClient apiClient = ServiceGenerator.tokenService(ApiClient.class, Constants.BASE_URL);

    @SuppressLint("CheckResult")
    public void getCustomer(String uuid, String customerName, Long WorkerBranchISN, Long WorkerISN) {
        Observable<Customer> customerObservable = apiClient.getCustomer(queryParams, customerName, uuid, WorkerBranchISN, WorkerISN
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        customerObservable.subscribe(customer -> {
            customerLiveData.setValue(customer);
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


    @SuppressLint("CheckResult")
    public void getSupplier(String uuid, String supplierName, Long WorkerBranchISN, Long WorkerISN) {
        Observable<Customer> supplierObservable = apiClient.getSupplier(queryParams, supplierName, uuid, WorkerBranchISN, WorkerISN

        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        supplierObservable.subscribe(customer -> {
            customerLiveData.setValue(customer);
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


    @SuppressLint("CheckResult")
    public void getSalesMan(String uuid, String salesManName, Long workerBranchISN, Long workerISN) {
        Observable<SalesMan> salesManObservable = apiClient.getSalesMan(queryParams, uuid, salesManName, workerBranchISN, workerISN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        salesManObservable.subscribe(salesMan -> {
            salesManLiveData.setValue(salesMan);
        }, throwable -> {
            throwable.printStackTrace();
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

    public void getCustomerBalance(String uuid, String dealerISN, String branchISN, String dealerType, String selectedBranchISN) {
        Observable<CustomerBalance> customerObservable = apiClient.getCustomerBalance(queryParams,  // Pass the common parameters as a map
                                                                                      uuid, dealerISN, branchISN, dealerType, selectedBranchISN,  // For Branch_ISN from spinner
                                                                                      currentUser.getInvoiceCurrentBalanceTimeInInvoice(), null,  // MoveBranchISN (optional)
                                                                                      null,  // Move_ISN (optional)
                                                                                      null,  // RemainValue (optional)
                                                                                      null,  // NetValue (optional)
                                                                                      null // MoveType (optional)
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


    private GeneralRequestBody generalRequestBody(){
        return new GeneralRequestBody(
                currentUser.getVendorID(),
                currentUser.getLogIn_BISN(),
                currentUser.getLogIn_UID(),
                currentUser.getLogIn_WBISN(),
                currentUser.getLogIn_WISN(),
                currentUser.getLogIn_WName(),
                currentUser.getLogIn_WSBISN(),
                currentUser.getLogIn_WSISN(),
                currentUser.getLogIn_WSName(),
                currentUser.getLogIn_CS(),
                currentUser.getLogIn_VN(),
                currentUser.getLogIn_FAlternative(),
                currentUser.getMobileSalesMaxDiscPer(),
                currentUser.getShiftSystemActivate(),
                currentUser.getLogIn_ShiftBranchISN(),
                currentUser.getLogIn_ShiftISN(),
                currentUser.getLogIn_Spare1(),
                currentUser.getLogIn_Spare2(),
                currentUser.getLogIn_Spare3(),
                currentUser.getLogIn_Spare4(),
                currentUser.getLogIn_Spare5(),
                currentUser.getLogIn_Spare6(),
                currentUser.getDeviceID(),
                currentUser.getLogIn_CurrentWorkingDayDate(),
                selectedFoundation, currentUser.getIllustrativeQuantity(), currentUser.getMobileC_D_B_W_W_C(), currentUser.getMobileOldShiftUse(), currentUser.getPermission(), currentUser.getMobileAllowCreateForward(), currentUser.getMobileAllowCreateForwardAmount(), currentUser.getMobileWS_AllowCreateForward(), currentUser.getMobileWS_AllowCreateForwardAmount(), currentUser.getLogIn_F_Spare1(), currentUser.getLogIn_F_Spare2(), currentUser.getLogIn_F_Spare3()
        );
    }

    @SuppressLint("CheckResult")
    public void getBranches(String uuid) {
        apiClient.getBranches(queryParams, uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(branches -> {
            branchesMutableLiveData.postValue(branches);

        }, throwable -> {
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
        });
    }

    @SuppressLint("CheckResult")
    public void getPriceType(String uuid) {
        Observable<PriceType> getPriceType = apiClient.getPriceType(
                queryParams, uuid, currentUser.getPricesTypeBranchISN(), currentUser.getPricesTypeISN(), (App.invoiceType == Sales || App.invoiceType == ReturnSales) ? 2 : 1
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getPriceType.subscribe(priceType -> {
            App.allPriceType = priceType.getData();
            boolean found = false;
            for (int i = 0; i < priceType.getData().size(); i++) {
                if (App.invoiceType == Sales || App.invoiceType == ReturnSales) {
                    if (priceType.getData().get(i).getBranchISN() == currentUser.getCashierSellPriceTypeBranchISN() && priceType.getData().get(i).getPricesType_ISN() == currentUser.getCashierSellPriceTypeISN()) {
                        priceTypeMutableLiveData.setValue(priceType.getData().get(i));
                        found = true;
                    }
                } else {
                    if (priceType.getData().get(i).getBasicPriceType() == 1) {
                        priceTypeMutableLiveData.setValue(priceType.getData().get(i));
                        found = true;
                    }
                }
            }
            if (!found) priceTypeMutableLiveData.postValue(priceType.getData().get(0));
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

    @SuppressLint("CheckResult")
    public void getDealerCategory(String uuid){
        Map<String, String> queryParams = GeneralRequestBodyUtil.toQueryParams();

        apiClient.getDealerCategories(queryParams, uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(category -> {
            dealerCategoriesResponseMutableLiveData.postValue(category);
        }, throwable -> {
            toastErrorMutableLiveData.postValue(throwable.getMessage());
        });
    }

}
