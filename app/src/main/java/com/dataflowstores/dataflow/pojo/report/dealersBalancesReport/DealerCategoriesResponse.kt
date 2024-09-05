package com.dataflowstores.dataflow.pojo.report.dealersBalancesReport


import com.google.gson.annotations.SerializedName

data class DealerCategoriesResponse(
    @SerializedName("data")
    val categories: List<DealerCategory?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)