package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PriceType implements Serializable {

    @SerializedName("status")
    int status;
    @SerializedName("data")
    List<PriceTypeData> data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PriceTypeData> getData() {
        return data;
    }

    public void setData(List<PriceTypeData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
