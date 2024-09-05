package com.dataflowstores.dataflow.ui.shifts.ui.openNewShift

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dataflowstores.dataflow.pojo.shifts.ShiftsResponse
import com.dataflowstores.dataflow.ui.shifts.ui.getGeneralBody
import com.dataflowstores.dataflow.webService.ApiClient
import com.dataflowstores.dataflow.webService.Constants
import com.dataflowstores.dataflow.webService.ServiceGenerator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.util.Objects

class ReopenShiftViewModel : ViewModel() {

    var shifsResponseLiveData = MutableLiveData<ShiftsResponse>()

    var toastErrorMutableLiveData = MutableLiveData<String>()

    var apiClient: ApiClient = ServiceGenerator.tokenService(
        ApiClient::class.java, Constants.BASE_URL
    )


    fun openClosedShift(
        uuid: String?,
        loginPass: String?,
        shiftISN: Long?,
        skipConfirmation: Int?,
    ) {
        apiClient.openClosedShift(
            getGeneralBody(),
            loginPass,
            shiftISN,
            skipConfirmation,
            uuid
        )
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :
                Observer<ShiftsResponse> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(result: ShiftsResponse) {
                    shifsResponseLiveData.value = result
                }

                override fun onError(throwable: Throwable) {
                    Log.e("error ", throwable.toString())
                    if (throwable is IOException) {
                        //handle network error
                        toastErrorMutableLiveData.postValue("No Internet Connection!")
                    } else if (throwable is HttpException) {
                        val errorBody =
                            Objects.requireNonNull((throwable as HttpException).response())!!
                                .errorBody()
                        try {
                            toastErrorMutableLiveData.postValue(
                                Objects.requireNonNull<ResponseBody?>(
                                    errorBody
                                ).string()
                            )
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                        //handle HTTP error response code
                    } else {
                        //handle other exceptions
                        toastErrorMutableLiveData.postValue(
                            Objects.requireNonNull<String?>(
                                throwable.message
                            )
                        )
                    }
                }

                override fun onComplete() {}
            })
    }


}