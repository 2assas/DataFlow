package com.dataflowstores.dataflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.PriceType;
import com.dataflowstores.dataflow.pojo.settings.PriceTypeData;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingVM extends ViewModel {
    public MutableLiveData<Stores> storesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Banks> banksMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PriceTypeData> priceTypeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PriceType> allPriceTypeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<SafeDeposit> safeDepositMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Branches> branchesMutableLiveData = new MutableLiveData<>();


    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);
    public void getStores(long branchISN, String uuid, int moveType){
            Observable<Stores> getStores
                    = apiClient.getStores(branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            getStores.subscribe(stores -> {
                storesMutableLiveData.setValue(stores);
            });
    }
    public void getStoresCashing(long branchISN, String uuid, int moveType){
            Observable<Stores> getStores
                    = apiClient.getStores(branchISN, 1, uuid, App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            getStores.subscribe(stores -> {
                storesMutableLiveData.setValue(stores);
            });
    }
    public void getBanks(long branchISN, String uuid){
        Observable<Banks> getBanks = apiClient.getBanks(branchISN, App.currentUser.getPermission(),uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBanks.subscribe(banks -> {banksMutableLiveData.setValue(banks);});
    }
    public void getSafeDeposit(long branchISN, String uuid, int moveType){
            Observable<SafeDeposit> getSafeDeposit = apiClient.getSafeDeposit(branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getSafeDepositBranchISN(), App.currentUser.getSafeDepositISN(), App.currentUser.getAllBranchesWorker(), moveType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            getSafeDeposit.subscribe(safeDeposit -> {
                safeDepositMutableLiveData.setValue(safeDeposit);
            });
         }
    public void getPriceType(String uuid){
            Observable<PriceType> getPriceType = apiClient.getPriceType(App.currentUser.getPermission(), uuid, App.currentUser.getPricesTypeBranchISN(), App.currentUser.getPricesTypeISN()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getPriceType.subscribe(priceType -> {
            for(int i=0; i<priceType.getData().size(); i++){
                if(priceType.getData().get(i).getBranchISN()==App.currentUser.getCashierSellPriceTypeBranchISN()&&
                        priceType.getData().get(i).getPricesType_ISN()==App.currentUser.getCashierSellPriceTypeISN()){
                    Log.e("checkPriceType","checked");
                    priceTypeMutableLiveData.setValue(priceType.getData().get(i));
                }
            }
           App.allPriceType= priceType.getData();
        });
    }

    public void getBranches(String uuid){
        Observable<Branches> getBranches = apiClient.getBranches(uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observer<Branches> observer = new Observer<Branches>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Branches branches) {
                branchesMutableLiveData.postValue(branches);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("branchesError", e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
        getBranches.subscribe(observer);
    }

}
