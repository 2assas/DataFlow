package com.dataflowstores.dataflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.product.Product;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.pojo.report.StoreReportModel;
import com.dataflowstores.dataflow.pojo.searchItemPrice.ItemPriceResponse;
import com.dataflowstores.dataflow.webService.ApiClient;
import com.dataflowstores.dataflow.webService.Constants;
import com.dataflowstores.dataflow.webService.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class  ProductVM extends ViewModel {
    public MutableLiveData<Product> productMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ProductData>> selectedProductMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<StoreReportModel> availableQuantityLiveData = new MutableLiveData<>();
    public MutableLiveData<ItemPriceResponse> itemPriceResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> toastErrorMutableLiveData = new MutableLiveData<>();

    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);


    public void getProduct(String productName, String uuid, String serial, int moveType){
        if(App.customer.getDealerName()==null){
            Observable<Product> productObservable = apiClient.getProduct(productName, uuid, App.priceType.getBranchISN(), App.priceType.getPricesType_ISN(), App.currentUser.getAllowSpecificDealersPrices(),(int) App.currentUser.getBranchISN(),moveType, serial)
                    .subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
            productObservable.subscribe(product -> {
                productMutableLiveData.postValue(product);
            });
        }else{
            Observable<Product> productObservable = apiClient.getProductCustomer(productName, uuid, App.priceType.getBranchISN(), App.priceType.getPricesType_ISN(), App.customer.getDealerType(), App.customer.getDealer_ISN(), App.customer.getBranchISN(), (int) App.currentUser.getBranchISN(), App.currentUser.getAllowSpecificDealersPrices(),moveType,serial)
                    .subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
            productObservable.subscribe(product -> {
                productMutableLiveData.postValue(product);
            },e -> {
                if (e instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) e).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(e.getMessage()));
                }
            });
        }
    }
    public void setSelectedProducts(ArrayList<ProductData> products){
        selectedProductMutableLiveData.setValue(products);
        Log.e("checkPost", "done");
    }

    public void checkAvailableQuantity(String uuid, Integer storeBranchISN, Integer storeISN, Integer itemBranchISN, Integer itemISN, Integer moveType){
        Observable<StoreReportModel> storeReportModelObservable= apiClient.getStoresReport(uuid, storeBranchISN, storeISN,itemBranchISN, itemISN, 1,null, moveType,App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(),App.currentUser.getWorkStationName(),String.valueOf( App.currentUser.getWorkStationISN()),String.valueOf( App.currentUser.getWorkerBranchISN()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        storeReportModelObservable.subscribe(new Observer<StoreReportModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StoreReportModel storeReportModel) {
                availableQuantityLiveData.setValue(storeReportModel);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("error ", e.toString());
                if (e instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) e).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getItemPrice(String uuid, int itemBranchISN, int itemISN){
        Observable<ItemPriceResponse> storeReportModelObservable= apiClient.getItemPrice(uuid,itemBranchISN, itemISN,App.currentUser.getWorkerName(),
                        App.currentUser.getUserName(),App.currentUser.getWorkStationName(),String.valueOf( App.currentUser.getWorkStationISN()),String.valueOf( App.currentUser.getWorkerBranchISN()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        storeReportModelObservable.subscribe(new Observer<ItemPriceResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ItemPriceResponse itemPriceResponse) {
                itemPriceResponseMutableLiveData.setValue(itemPriceResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("error ", e.toString());
                if (e instanceof HttpException) {
                    ResponseBody errorBody = ((HttpException) e).response().errorBody();
                    try {
                        toastErrorMutableLiveData.postValue(Objects.requireNonNull(errorBody).string());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    toastErrorMutableLiveData.postValue(Objects.requireNonNull(e.getMessage()));
                }
            }
            @Override
            public void onComplete() {

            }
        });
    }

}
