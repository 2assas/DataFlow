package com.dataflowstores.dataflow.pojo.report.dealersBalancesReport


import com.google.gson.annotations.SerializedName

data class DealerCategory(
    @SerializedName("DealerCategory")
    val dealerCategory: String?,
    @SerializedName("DealerType")
    val dealerType: String?
)