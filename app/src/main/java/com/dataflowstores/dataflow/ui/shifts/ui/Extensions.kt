package com.dataflowstores.dataflow.ui.shifts.ui

import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.pojo.GeneralRequestBody

 fun getGeneralBody(): GeneralRequestBody {
    val user = App.currentUser
    return GeneralRequestBody(
        VendorID = user.vendorID,
        LogIn_BISN = user.logIn_BISN,
        LogIn_UID = user.logIn_UID,
        LogIn_WBISN = user.logIn_WBISN,
        LogIn_WISN = user.logIn_WISN,
        LogIn_WName = user.logIn_WName,
        LogIn_WSBISN = user.logIn_WSBISN,
        LogIn_WSISN = user.logIn_WSISN,
        LogIn_WSName = user.logIn_WSName,
        LogIn_CS = user.logIn_CS,
        LogIn_VN = user.logIn_VN,
        LogIn_FAlternative = user.logIn_FAlternative,
        MobileSalesMaxDiscPer = user.mobileSalesMaxDiscPer,
        ShiftSystemActivate = user.shiftSystemActivate,
        LogIn_ShiftBranchISN = user.logIn_ShiftBranchISN,
        LogIn_ShiftISN = user.logIn_ShiftISN,
        LogIn_Spare1 = user.logIn_Spare1,
        LogIn_Spare2 = user.logIn_Spare2,
        LogIn_Spare3 = user.logIn_Spare3,
        LogIn_Spare4 = user.logIn_Spare4,
        LogIn_Spare5 = user.logIn_Spare5,
        LogIn_Spare6 = user.logIn_Spare6,
        DeviceID = user.deviceID,
        LogIn_CurrentWorkingDayDate = user.logIn_CurrentWorkingDayDate,
        App.selectedFoundation,
        App.currentUser.illustrativeQuantity,
    )
}
