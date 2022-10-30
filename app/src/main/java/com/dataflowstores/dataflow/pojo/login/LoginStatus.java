package com.dataflowstores.dataflow.pojo.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginStatus implements Serializable {
    @SerializedName("status")
    private Long status;
    @SerializedName("data")
    private UserData data;
    @SerializedName("message")
    private String message;

    public Long getStatus() { return status; }
    public void setStatus(Long value) { this.status = value; }

    public UserData getData() { return data; }
    public void setData(UserData value) { this.data = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
}
