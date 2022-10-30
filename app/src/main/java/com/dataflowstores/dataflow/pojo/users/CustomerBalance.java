package com.dataflowstores.dataflow.pojo.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerBalance implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private String data;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
