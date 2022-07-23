package com.example.dataflow.ViewModels;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.dataflow.pojo.login.LoginStatus;
import com.example.dataflow.pojo.workStation.Branch;
import com.example.dataflow.pojo.workStation.Workstation;
import com.example.dataflow.pojo.workStation.WorkstationList;
import com.example.dataflow.utils.Conts;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import java.net.InetAddress;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GateWayViewModel extends ViewModel {
    public MutableLiveData<LoginStatus> loginLiveData = new MutableLiveData<>();
    public MutableLiveData<WorkstationList> workStationLiveData = new MutableLiveData<>();
    public MutableLiveData<Workstation> insertWorkstationLiveData = new MutableLiveData<>();
    public MutableLiveData<Branch> insertBranchLiveData = new MutableLiveData<>();
    ApiClient apiClient = ServiceGenerator.newService(
            ApiClient.class, Constants.BASE_URL);

    public void getLoginStatus(String uuid, String user_name, String password, String foundation_name, String phone){
        Observable<LoginStatus> login= apiClient.loginGateWay(uuid,user_name ,password ,foundation_name,phone,2, Conts.APP_VERSION).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        login.subscribe(loginStatus -> {
            loginLiveData.setValue(loginStatus);
        });
    }
    public void createWorkstation(String uuid){
        Observable<WorkstationList> workstation= apiClient.createWorkstation(uuid,2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        workstation.subscribe(workstationObserver ->{
            workStationLiveData.setValue(workstationObserver);
        });
    }

    public void insertWorkstation(String uuid, long branchId, String workStationName){
        Observable<Workstation> insertWorkstation= apiClient.insertWorkstation(uuid, branchId, workStationName, 2).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        insertWorkstation.subscribe(workstation -> {
            insertWorkstationLiveData.setValue(workstation);
        });
    }

    public void insertBranch(int branchNumber, String branchName, String uuid){
        Observable<Branch> insertBranch = apiClient.insertBranch(branchNumber, branchName, uuid, 2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        insertBranch.subscribe(branch ->{
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
}
