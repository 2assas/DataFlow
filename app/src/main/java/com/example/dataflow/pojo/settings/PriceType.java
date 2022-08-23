package com.example.dataflow.pojo.settings;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriceType implements Serializable {

    @SerializedName("status")
    int status;
    @SerializedName("data")
    ArrayList<PriceTypeData> data;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<PriceTypeData> getData() {
        return data;
    }

    public void setData(ArrayList<PriceTypeData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
