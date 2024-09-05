package com.dataflowstores.dataflow.utils;

import static com.dataflowstores.dataflow.App.currentUser;
import static com.dataflowstores.dataflow.App.selectedFoundation;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.GeneralRequestBody;

public class GeneralParams {

    public GeneralRequestBody generalRequestBody(){
        return new GeneralRequestBody(
                currentUser.getVendorID(),
                currentUser.getLogIn_BISN(),
                currentUser.getLogIn_UID(),
                currentUser.getLogIn_WBISN(),
                currentUser.getLogIn_WISN(),
                currentUser.getLogIn_WName(),
                currentUser.getLogIn_WSBISN(),
                currentUser.getLogIn_WSISN(),
                currentUser.getLogIn_WSName(),
                currentUser.getLogIn_CS(),
                currentUser.getLogIn_VN(),
                currentUser.getLogIn_FAlternative(),
                currentUser.getMobileSalesMaxDiscPer(),
                currentUser.getShiftSystemActivate(),
                currentUser.getLogIn_ShiftBranchISN(),
                currentUser.getLogIn_ShiftISN(),
                currentUser.getLogIn_Spare1(),
                currentUser.getLogIn_Spare2(),
                currentUser.getLogIn_Spare3(),
                currentUser.getLogIn_Spare4(),
                currentUser.getLogIn_Spare5(),
                currentUser.getLogIn_Spare6(),
                currentUser.getDeviceID(),
                currentUser.getLogIn_CurrentWorkingDayDate(),
                selectedFoundation,
                App.currentUser.getIllustrativeQuantity()
        );
    }

}
