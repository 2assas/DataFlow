package com.example.dataflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dataflow.App;
import com.example.dataflow.pojo.product.Product;
import com.example.dataflow.pojo.product.ProductData;
import com.example.dataflow.pojo.settings.Stores;
import com.example.dataflow.webService.ApiClient;
import com.example.dataflow.webService.Constants;
import com.example.dataflow.webService.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class  ProductVM extends ViewModel {
    public MutableLiveData<Product> productMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ProductData>> selectedProductMutableLiveData = new MutableLiveData<>();
    ApiClient apiClient = ServiceGenerator.tokenService(
            ApiClient.class, Constants.BASE_URL);


    public void getProduct(String productName, String uuid, String serial){
        if(App.customer.getDealerName()==null){
            Observable<Product> productObservable = apiClient.getProduct(productName, uuid, App.currentUser.getPricesTypeBranchISN(), App.currentUser.getPricesTypeISN(), App.currentUser.getAllowSpecificDealersPrices(), serial)
                    .subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
            productObservable.subscribe(product -> {
                productMutableLiveData.postValue(product);
            });
        }else{
            Observable<Product> productObservable = apiClient.getProductCustomer(productName, uuid, App.currentUser.getPricesTypeBranchISN(), App.currentUser.getPricesTypeISN(), App.customer.getDealerType(), App.customer.getDealer_ISN(), App.customer.getBranchISN(), App.currentUser.getAllowSpecificDealersPrices(),serial)
                    .subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
            productObservable.subscribe(product -> {
                productMutableLiveData.postValue(product);
            });
        }
    }
    public void setSelectedProducts(ArrayList<ProductData> products){
        selectedProductMutableLiveData.setValue(products);
        Log.e("checkPost", "done");
    }





}
