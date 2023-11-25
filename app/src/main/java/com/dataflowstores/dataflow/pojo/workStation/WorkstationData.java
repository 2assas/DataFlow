package com.dataflowstores.dataflow.pojo.workStation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkstationData implements Serializable {
    @SerializedName("Branch_ISN")
    String Branch_ISN;
    @SerializedName("WorkStation_ISN")
    String workstationISN;
    @SerializedName("WorkStationName")
    String workstationName;

    public String getBranch_ISN() {
        return Branch_ISN;
    }

    public void setBranch_ISN(String branch_ISN) {
        Branch_ISN = branch_ISN;
    }

    public String getWorkstationISN() {
        return workstationISN;
    }

    public void setWorkstationISN(String workstationISN) {
        this.workstationISN = workstationISN;
    }

    public String getWorkstationName() {
        return workstationName;
    }

    public void setWorkstationName(String workstationName) {
        this.workstationName = workstationName;
    }
}
