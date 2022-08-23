package com.example.dataflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dataflow.App;
import com.example.dataflow.pojo.settings.Banks;
import com.example.dataflow.pojo.settings.PriceType;
import com.example.dataflow.pojo.settings.PriceTypeData;
import com.example.dataflow.pojo.settings.SafeDeposit;
import com.example.dataflow.pojo.settings.Stores;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingVM extends ViewModel {
    public MutableLiveData<Stores> storesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Banks> banksMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PriceTypeData> priceTypeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PriceType> allPriceTypeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<SafeDeposit> safeDepositMutableLiveData = new MutableLiveData<>();
    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);
    public void getStores(long branchISN, String uuid){
            Observable<Stores> getStores
                    = apiClient.getStores(branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            getStores.subscribe(stores -> {
                storesMutableLiveData.setValue(stores);
            });
    }
    public void getStoresCashing(long branchISN, String uuid){
            Observable<Stores> getStores
                    = apiClient.getStores(branchISN, 1, uuid, App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            getStores.subscribe(stores -> {
                storesMutableLiveData.setValue(stores);
            });
    }
    public void getBanks(long branchISN, String uuid){
        Observable<Banks> getBanks = apiClient.getBanks(branchISN, App.currentUser.getPermission(),uuid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBanks.subscribe(banks -> {banksMutableLiveData.setValue(banks);});
    }
    public void getSafeDeposit(long branchISN, String uuid){
            Observable<SafeDeposit> getSafeDeposit = apiClient.getSafeDeposit(branchISN, App.currentUser.getPermission(), uuid, App.currentUser.getSafeDepositBranchISN(), App.currentUser.getSafeDepositISN()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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
}
