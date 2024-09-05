package com.dataflowstores.dataflow.pojo.report.dealersBalancesReport

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.pojo.GeneralRequestBody
import com.dataflowstores.dataflow.utils.SingleLiveEvent
import com.dataflowstores.dataflow.webService.ApiClient
import com.dataflowstores.dataflow.webService.Constants
import com.dataflowstores.dataflow.webService.ServiceGenerator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class DealerViewModel() : ViewModel() {

    private val _dealers = SingleLiveEvent<List<DealersBalancesData>?>()
    val dealers: SingleLiveEvent<List<DealersBalancesData>?> = _dealers
    var toastErrorMutableLiveData = MutableLiveData<String>()
    var currentPage = 0
    var isLoading = false
    var apiClient: ApiClient = ServiceGenerator.tokenService(
        ApiClient::class.java, Constants.BASE_URL
    )
    private var disposable: Disposable? = null

    @SuppressLint("CheckResult")
    fun getDealersBalancesReport(
        dealerName: String?,
        uuid: String?,
        branchISN: String?,
        dealerType: Int?,
        workerBranchISN: String?,
        excludeZeroBalance: Boolean?,
        dealerCategory: String
    ) {
        if (isLoading) return
        isLoading = true
        disposable = apiClient.dealersBalancesReport(
            generalRequestBody(),
            dealerName,
            uuid,
            branchISN,
            dealerType,
            workerBranchISN,
            currentPage,
            if (excludeZeroBalance == true) 1 else 0,
            dealerCategory
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                isLoading = false
                if (response.status == 1) {
                    val currentData = _dealers.value ?: emptyList()
                    val newData = currentData.plus(response.data)
                    _dealers.value = newData
                } else _dealers.value = null

            }, { throwable ->
                isLoading = false
                toastErrorMutableLiveData.postValue(throwable.message)
                _dealers.value = null
                // Handle error
            })
        currentPage++
    }

    fun loadMoreData(
        dealerName: String?,
        uuid: String?,
        branchISN: String?,
        dealerType: Int?,
        workerBranchISN: String?,
        excludeZeroBalance: Boolean?,
        dealerCategory: String
    ) {
        getDealersBalancesReport(
            dealerName,
            uuid,
            branchISN,
            dealerType,
            workerBranchISN,
            excludeZeroBalance,
            dealerCategory
        )
    }

    private fun generalRequestBody(): GeneralRequestBody {
        return GeneralRequestBody(
            App.currentUser.vendorID,
            App.currentUser.logIn_BISN,
            App.currentUser.logIn_UID,
            App.currentUser.logIn_WBISN,
            App.currentUser.logIn_WISN,
            App.currentUser.logIn_WName,
            App.currentUser.logIn_WSBISN,
            App.currentUser.logIn_WSISN,
            App.currentUser.logIn_WSName,
            App.currentUser.logIn_CS,
            App.currentUser.logIn_VN,
            App.currentUser.logIn_FAlternative,
            App.currentUser.mobileSalesMaxDiscPer,
            App.currentUser.shiftSystemActivate,
            App.currentUser.logIn_ShiftBranchISN,
            App.currentUser.logIn_ShiftISN,
            App.currentUser.logIn_Spare1,
            App.currentUser.logIn_Spare2,
            App.currentUser.logIn_Spare3,
            App.currentUser.logIn_Spare4,
            App.currentUser.logIn_Spare5,
            App.currentUser.logIn_Spare6,
            App.currentUser.deviceID,
            App.currentUser.logIn_CurrentWorkingDayDate,
            App.selectedFoundation,
            App.currentUser.illustrativeQuantity,
        )
    }

    fun dispose() {
        currentPage = 0
        isLoading = false
        Log.e("checkLoadMore", "dispose")
        dealers.value = listOf()
        disposable?.dispose()// Cancel all disposables
    }

}

