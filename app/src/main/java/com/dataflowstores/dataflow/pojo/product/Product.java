package com.dataflowstores.dataflow.pojo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    @SerializedName("status")
    int status;
    @SerializedName("data")
    ArrayList<ProductData> data;
    @SerializedName("message")
    String message;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<ProductData> getData() {
        return data;
    }

    public void setData(ArrayList<ProductData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
