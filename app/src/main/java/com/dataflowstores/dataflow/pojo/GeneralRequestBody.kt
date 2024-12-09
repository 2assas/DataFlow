package com.dataflowstores.dataflow.pojo

import com.dataflowstores.dataflow.utils.GeneralParams
import java.util.function.Function
import java.util.stream.Collectors



data class GeneralRequestBody(
    val VendorID: Long? = null,
    val LogIn_BISN: String? = null,
    val LogIn_UID: String? = null,
    val LogIn_WBISN: String? = null,
    val LogIn_WISN: String? = null,
    val LogIn_WName: String? = null,
    val LogIn_WSBISN: String? = null,
    val LogIn_WSISN: String? = null,
    val LogIn_WSName: String? = null,
    val LogIn_CS: String? = null,
    val LogIn_VN: String? = null,
    val LogIn_FAlternative: String? = null,
    val MobileSalesMaxDiscPer: String? = null,
    val ShiftSystemActivate: Int? = null,
    val LogIn_ShiftBranchISN: Int? = null,
    val LogIn_ShiftISN: Int? = null,
    val LogIn_Spare1: Int? = null,
    val LogIn_Spare2: Int? = null,
    val LogIn_Spare3: Int? = null,
    val LogIn_Spare4: Int? = null,
    val LogIn_Spare5: Int? = null,
    val LogIn_Spare6: Int? = null,
    val DeviceID: String? = null,
    val LogIn_CurrentWorkingDayDate: String? = null,
    val SelectedFoundation: Int? = null,
    val IllustrativeQuantity: Int? = null,
    val MobileC_D_B_W_W_C: String? = null,
    val MobileOldShiftUse: String? = null,
    val permission: Int? = null,
    val MobileAllowCreateForward: String? = null,
    val MobileAllowCreateForwardAmount: Float? = null,
    val MobileWS_AllowCreateForward: String? = null,
    val MobileWS_AllowCreateForwardAmount: Float? = null,
    val LogIn_F_Spare1: Float? = null,
    val LogIn_F_Spare2: Float? = null,
    val LogIn_F_Spare3: Float? = null
)

object GeneralRequestBodyUtil {
    @JvmStatic
    fun toQueryParams(): Map<String, String?> {
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
            "IllustrativeQuantity" to requestBody.IllustrativeQuantity.toString(),
            "MobileC_D_B_W_W_C" to requestBody.MobileC_D_B_W_W_C,
            "MobileOldShiftUse" to requestBody.MobileOldShiftUse,
            "permission" to requestBody.permission.toString(),
            "MobileAllowCreateForward" to requestBody.MobileAllowCreateForward,
            "MobileAllowCreateForwardAmount" to requestBody.MobileAllowCreateForwardAmount.toString(),
            "MobileWS_AllowCreateForward" to requestBody.MobileWS_AllowCreateForward,
            "MobileWS_AllowCreateForwardAmount" to requestBody.MobileWS_AllowCreateForwardAmount.toString(),
            "LogIn_F_Spare1" to requestBody.LogIn_F_Spare1.toString(),
            "LogIn_F_Spare2" to requestBody.LogIn_F_Spare2.toString(),
            "LogIn_F_Spare3" to requestBody.LogIn_F_Spare3.toString()
        ).filterValues { it != null  && it != "null"} // Remove entries with null values
            .mapValues { it.value!! }
    }
}
fun GeneralRequestBody.toQueryParams(): Map<String, String?> {
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
        "MobileSalesMaxDiscPer" to MobileSalesMaxDiscPer,
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
        "IllustrativeQuantity" to IllustrativeQuantity.toString(),
        "MobileC_D_B_W_W_C" to MobileC_D_B_W_W_C,
        "MobileOldShiftUse" to MobileOldShiftUse,
        "permission" to permission.toString(),
        "MobileAllowCreateForward" to MobileAllowCreateForward,
        "MobileAllowCreateForwardAmount" to MobileAllowCreateForwardAmount.toString(),
        "MobileWS_AllowCreateForwardAmount" to MobileWS_AllowCreateForwardAmount.toString(),
        "MobileWS_AllowCreateForward" to MobileWS_AllowCreateForward.toString(),
        "LogIn_F_Spare1" to LogIn_F_Spare1.toString(),
        "LogIn_F_Spare2" to LogIn_F_Spare2.toString(),
        "LogIn_F_Spare3" to LogIn_F_Spare3.toString()
    ).filterValues { it != null  && it != "null"} // Remove entries with null values
        .mapValues { it.value!! }
}
