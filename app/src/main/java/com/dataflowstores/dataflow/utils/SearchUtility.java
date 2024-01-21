package com.dataflowstores.dataflow.utils;

import static com.dataflowstores.dataflow.App.selectedFoundation;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.product.SearchProductResponse;
import com.dataflowstores.dataflow.webService.ApiClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchUtility {
    @SuppressLint("CheckResult")
    public static LiveData<List<SearchProductResponse>> searchQuery(
            Observable<String> searchObservable,
            ApiClient apiClient,
            String uuid
    ) {
        MutableLiveData<List<SearchProductResponse>> searchResults = new MutableLiveData<>();

        searchObservable
                .debounce(App.currentUser.getMobileSearchMilliSecondsWait(), TimeUnit.MILLISECONDS)
                .filter(query -> query.length() >= App.currentUser.getMobileSearchTypeLetterCount())
                .distinctUntilChanged()
                .observeOn(Schedulers.io())
                .subscribe(query -> performSearch(apiClient, query, uuid, selectedFoundation, searchResults));

        return searchResults;
    }

    @SuppressLint("CheckResult")
    private static void performSearch(ApiClient apiClient, String query, String uuid, Integer selectedFoundation, MutableLiveData<List<SearchProductResponse>> searchResults) {
        apiClient.searchProduct(query, uuid, selectedFoundation)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(searchProductResponse -> {
                            searchResults.postValue(searchProductResponse.getDataList());
                        }, throwable -> {
                            searchResults.postValue(null);
                            Log.e("checkError", "error in Search = " + throwable.getMessage());
                        }
                );
    }
}
