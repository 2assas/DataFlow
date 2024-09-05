package com.dataflowstores.dataflow.pojo

import com.dataflowstores.dataflow.utils.GeneralParams

data class GeneralRequestBody(
    val VendorID: Long,
    val LogIn_BISN: String,
    val LogIn_UID: String,
    val LogIn_WBISN: String,
    val LogIn_WISN: String,
    val LogIn_WName: String,
    val LogIn_WSBISN: String,
    val LogIn_WSISN: String,
    val LogIn_WSName: String,
    val LogIn_CS: String,
    val LogIn_VN: String,
    val LogIn_FAlternative: String,
    val MobileSalesMaxDiscPer: Int,
    val ShiftSystemActivate: Int,
    val LogIn_ShiftBranchISN: Int,
    val LogIn_ShiftISN: Int,
    val LogIn_Spare1: Int,
    val LogIn_Spare2: Int,
    val LogIn_Spare3: Int,
    val LogIn_Spare4: Int,
    val LogIn_Spare5: Int,
    val LogIn_Spare6: Int,
    val DeviceID: String,
    val LogIn_CurrentWorkingDayDate: String,
    val SelectedFoundation: Int,
    val IllustrativeQuantity: Int
)

object GeneralRequestBodyUtil {
    @JvmStatic
    fun toQueryParams(): Map<String, String> {
        val generalParams = GeneralParams()
        val requestBody = generalParams.generalRequestBody()
        return mapOf(
            "VendorID" to requestBody.VendorID.toString(),
            "LogIn_BISN" to requestBody.LogIn_BISN,
            "LogIn_UID" to requestBody.LogIn_UID,
            "LogIn_WBISN" to requestBody.LogIn_WBISN,
            "LogIn_WISN" to requestBody.LogIn_WISN,
            "LogIn_WName" to requestBody.LogIn_WName,
            "LogIn_WSBISN" to requestBody.LogIn_WSBISN,
            "LogIn_WSISN" to requestBody.LogIn_WSISN,
            "LogIn_WSName" to requestBody.LogIn_WSName,
            "LogIn_CS" to requestBody.LogIn_CS,
            "LogIn_VN" to requestBody.LogIn_VN,
            "LogIn_FAlternative" to requestBody.LogIn_FAlternative,
            "MobileSalesMaxDiscPer" to requestBody.MobileSalesMaxDiscPer.toString(),
            "ShiftSystemActivate" to requestBody.ShiftSystemActivate.toString(),
            "LogIn_ShiftBranchISN" to requestBody.LogIn_ShiftBranchISN.toString(),
            "LogIn_ShiftISN" to requestBody.LogIn_ShiftISN.toString(),
            "LogIn_Spare1" to requestBody.LogIn_Spare1.toString(),
            "LogIn_Spare2" to requestBody.LogIn_Spare2.toString(),
            "LogIn_Spare3" to requestBody.LogIn_Spare3.toString(),
            "LogIn_Spare4" to requestBody.LogIn_Spare4.toString(),
            "LogIn_Spare5" to requestBody.LogIn_Spare5.toString(),
            "LogIn_Spare6" to requestBody.LogIn_Spare6.toString(),
            "DeviceID" to requestBody.DeviceID,
            "LogIn_CurrentWorkingDayDate" to requestBody.LogIn_CurrentWorkingDayDate,
            "SelectedFoundation" to requestBody.SelectedFoundation.toString(),
            "IllustrativeQuantity" to requestBody.IllustrativeQuantity.toString()
        )
    }
}
fun GeneralRequestBody.toQueryParams(): Map<String, String> {
    return mapOf(
        "VendorID" to VendorID.toString(),
        "LogIn_BISN" to LogIn_BISN,
        "LogIn_UID" to LogIn_UID,
        "LogIn_WBISN" to LogIn_WBISN,
        "LogIn_WISN" to LogIn_WISN,
        "LogIn_WName" to LogIn_WName,
        "LogIn_WSBISN" to LogIn_WSBISN,
        "LogIn_WSISN" to LogIn_WSISN,
        "LogIn_WSName" to LogIn_WSName,
        "LogIn_CS" to LogIn_CS,
        "LogIn_VN" to LogIn_VN,
        "LogIn_FAlternative" to LogIn_FAlternative,
        "MobileSalesMaxDiscPer" to MobileSalesMaxDiscPer.toString(),
        "ShiftSystemActivate" to ShiftSystemActivate.toString(),
        "LogIn_ShiftBranchISN" to LogIn_ShiftBranchISN.toString(),
        "LogIn_ShiftISN" to LogIn_ShiftISN.toString(),
        "LogIn_Spare1" to LogIn_Spare1.toString(),
        "LogIn_Spare2" to LogIn_Spare2.toString(),
        "LogIn_Spare3" to LogIn_Spare3.toString(),
        "LogIn_Spare4" to LogIn_Spare4.toString(),
        "LogIn_Spare5" to LogIn_Spare5.toString(),
        "LogIn_Spare6" to LogIn_Spare6.toString(),
        "DeviceID" to DeviceID,
        "LogIn_CurrentWorkingDayDate" to LogIn_CurrentWorkingDayDate,
        "SelectedFoundation" to SelectedFoundation.toString(),
        "IllustrativeQuantity" to IllustrativeQuantity.toString()
    )
}
