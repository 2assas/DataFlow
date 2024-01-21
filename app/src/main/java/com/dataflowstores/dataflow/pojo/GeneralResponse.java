package com.dataflowstores.dataflow.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneralResponse<T> implements Serializable {
    @SerializedName("status")
    Integer status;
    @SerializedName("data")
    ArrayList<T> dataList;
    @SerializedName("message")
    String message;

    public GeneralResponse(Integer status, ArrayList<T> dataList, String message) {
        this.status = status;
        this.dataList = dataList;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
