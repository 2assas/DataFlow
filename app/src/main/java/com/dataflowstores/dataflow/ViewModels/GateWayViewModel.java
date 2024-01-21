package com.dataflowstores.dataflow.ViewModels;

import static com.dataflowstores.dataflow.App.selectedFoundation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.login.BranchStaffModel;
import com.dataflowstores.dataflow.pojo.login.LoginStatus;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;
import com.dataflowstores.dataflow.pojo.workStation.Branch;
import com.dataflowstores.dataflow.pojo.workStation.Workstation;
import com.dataflowstores.dataflow.pojo.workStation.WorkstationList;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class GateWayViewModel extends ViewModel {
    public MutableLiveData<LoginStatus> loginLiveData = new MutableLiveData<>();
    public MutableLiveData<WorkstationList> workStationLiveData = new MutableLiveData<>();
    public MutableLiveData<Workstation> insertWorkstationLiveData = new MutableLiveData<>();
    public MutableLiveData<Branch> insertBranchLiveData = new MutableLiveData<>();
    public MutableLiveData<BranchStaffModel> branchStaffMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();


    ApiClient apiClient = ServiceGenerator.newService(
            ApiClient.class, Constants.BASE_URL);


    public void getLoginStatus(String uuid, String user_name, String password, String foundation_name, String phone, String selectedBranchISN, String selectedSafeDepositBranchISN,
                               String selectedSafeDepositISN, String selectedStoreBranchISN, String selectedStoreISN, int demo) {
        Observable<LoginStatus> login = apiClient.loginGateWay(uuid, user_name, password, foundation_name, phone, 2, Constants.APP_VERSION,
                selectedBranchISN, selectedSafeDepositBranchISN, selectedSafeDepositISN, selectedStoreBranchISN, selectedStoreISN, demo, selectedFoundation
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        login.subscribe(loginStatus -> {
            loginLiveData.setValue(loginStatus);
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

    public void createWorkstation(String uuid) {
        Observable<WorkstationList> workstation = apiClient.createWorkstation(uuid, 2, selectedFoundation).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        workstation.subscribe(workstationObserver -> {
            workStationLiveData.setValue(workstationObserver);
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

    public void insertWorkstation(String uuid, long branchId, String workStationName) {
        Observable<Workstation> insertWorkstation = apiClient.insertWorkstation(uuid, branchId, workStationName, 2, selectedFoundation).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        insertWorkstation.subscribe(workstation -> {
            insertWorkstationLiveData.setValue(workstation);
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

    public void insertBranch(int branchNumber, String branchName, String uuid) {

        Observable<Branch> insertBranch = apiClient.insertBranch(branchNumber, branchName, uuid, 2, selectedFoundation).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        insertBranch.subscribe(branch -> {
            insertBranchLiveData.setValue(branch);
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

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public void SelectBranchStaff(String uuid, int moveType) {
        ApiClient tokenService = ServiceGenerator.tokenService(
                ApiClient.class, Constants.BASE_URL);
        Observable<Branches> getBranches = tokenService.getBranches(uuid, selectedFoundation,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
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
        Observable<Stores> getStores = tokenService.getStores(null, 1, uuid, null, null, -1, moveType, selectedFoundation,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
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
        Observable<SafeDeposit> getSafeDeposits = tokenService.getSafeDeposit(null, 1, uuid, null, null, -1, moveType, selectedFoundation,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
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

        Observable<BranchStaffModel> zipper = Observable.zip(getBranches, getStores, getSafeDeposits, BranchStaffModel::new);

        zipper.subscribe(new Observer<BranchStaffModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull BranchStaffModel branchStaffModel) {
                branchStaffMutableLiveData.postValue(branchStaffModel);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e("checkError", throwable.getMessage());
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
