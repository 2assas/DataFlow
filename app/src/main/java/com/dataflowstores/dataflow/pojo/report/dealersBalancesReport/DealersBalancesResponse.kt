package com.dataflowstores.dataflow.pojo.report.dealersBalancesReport


import com.google.gson.annotations.SerializedName

data class DealersBalancesResponse(
    @SerializedName("data")
    val data: List<DealersBalancesData>,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)
data class DealersBalancesData(
    @SerializedName("CurrentBalance")
    val currentBalance: String?,
    @SerializedName("DealerBranchISN")
    val dealerBranchISN: Int?,
    @SerializedName("DealerISN")
    val dealerISN: Int?,
    @SerializedName("DealerName")
    val dealerName: String?,
    @SerializedName("DealerType")
    val dealerType: Int?,
    @SerializedName("InitialBalance")
    val initialBalance: String?
)