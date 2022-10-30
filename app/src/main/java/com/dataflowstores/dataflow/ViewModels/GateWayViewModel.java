package com.dataflowstores.dataflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.login.BranchStaffModel;
import com.dataflowstores.dataflow.webService.ServiceGenerator;
import com.dataflowstores.dataflow.pojo.login.LoginStatus;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;
import com.dataflowstores.dataflow.pojo.workStation.Branch;
import com.dataflowstores.dataflow.pojo.workStation.Workstation;
import com.dataflowstores.dataflow.pojo.workStation.WorkstationList;
import com.dataflowstores.dataflow.utils.Conts;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;

import java.net.InetAddress;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GateWayViewModel extends ViewModel {
    public MutableLiveData<LoginStatus> loginLiveData = new MutableLiveData<>();
    public MutableLiveData<WorkstationList> workStationLiveData = new MutableLiveData<>();
    public MutableLiveData<Workstation> insertWorkstationLiveData = new MutableLiveData<>();
    public MutableLiveData<Branch> insertBranchLiveData = new MutableLiveData<>();
    public MutableLiveData<BranchStaffModel> branchStaffMutableLiveData = new MutableLiveData<>();


    ApiClient apiClient = ServiceGenerator.newService(
            ApiClient.class, Constants.BASE_URL);


    public void getLoginStatus(String uuid, String user_name, String password, String foundation_name, String phone, String selectedBranchISN, String selectedSafeDepositBranchISN,
                               String selectedSafeDepositISN, String selectedStoreBranchISN, String selectedStoreISN, int demo) {
        Observable<LoginStatus> login = apiClient.loginGateWay(uuid, user_name, password, foundation_name, phone, 2, Conts.APP_VERSION,
                selectedBranchISN, selectedSafeDepositBranchISN, selectedSafeDepositISN, selectedStoreBranchISN, selectedStoreISN, demo).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        login.subscribe(loginStatus -> {
            loginLiveData.setValue(loginStatus);
        });
    }

    public void createWorkstation(String uuid) {
        Observable<WorkstationList> workstation = apiClient.createWorkstation(uuid, 2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        workstation.subscribe(workstationObserver -> {
            workStationLiveData.setValue(workstationObserver);
        });
    }

    public void insertWorkstation(String uuid, long branchId, String workStationName) {
        Observable<Workstation> insertWorkstation = apiClient.insertWorkstation(uuid, branchId, workStationName, 2).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        insertWorkstation.subscribe(workstation -> {
            insertWorkstationLiveData.setValue(workstation);
        });
    }

    public void insertBranch(int branchNumber, String branchName, String uuid) {

        Observable<Branch> insertBranch = apiClient.insertBranch(branchNumber, branchName, uuid, 2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        insertBranch.subscribe(branch -> {
            insertBranchLiveData.setValue(branch);
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
        Observable<Branches> getBranches = tokenService.getBranches(uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<Stores> getStores = tokenService.getStores(null, 1, uuid, null, null, -1, moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observable<SafeDeposit> getSafeDeposits = tokenService.getSafeDeposit(null, 1, uuid, null, null, -1, moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

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
            public void onError(@NonNull Throwable e) {
                Log.e("checkError", e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }


}
