package com.dataflowstores.dataflow.pojo.settings;

import android.os.Parcel;
import android.os.Parcelable;

import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PriceTypeData implements Serializable, Parcelable {
    @SerializedName("BranchISN")
    int BranchISN;
    @SerializedName("PricesType_ISN")
    int PricesType_ISN;
    @SerializedName("PricesTypeISNBranch")
    int PricesTypeISNBranch;
    @SerializedName("PricesTypeName")
    String PricesTypeName;
    @SerializedName("Type")
    String Type;

    @SerializedName("BasicPriceType")
    int BasicPriceType;

    public int getBasicPriceType() {
        return BasicPriceType;
    }

    public void setBasicPriceType(int basicPriceType) {
        BasicPriceType = basicPriceType;
    }

    public int getBranchISN() {
        return BranchISN;
    }

    public void setBranchISN(int branchISN) {
        BranchISN = branchISN;
    }

    public int getPricesType_ISN() {
        return PricesType_ISN;
    }

    public void setPricesType_ISN(int pricesType_ISN) {
        PricesType_ISN = pricesType_ISN;
    }

    public int getPricesTypeISNBranch() {
        return PricesTypeISNBranch;
    }

    public void setPricesTypeISNBranch(int pricesTypeISNBranch) {
        PricesTypeISNBranch = pricesTypeISNBranch;
    }

    public String getPricesTypeName() {
        return PricesTypeName;
    }

    public void setPricesTypeName(String pricesTypeName) {
        PricesTypeName = pricesTypeName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.BranchISN);
        dest.writeInt(this.PricesType_ISN);
        dest.writeInt(this.PricesTypeISNBranch);
        dest.writeString(this.PricesTypeName);
        dest.writeString(this.Type);
        dest.writeInt(this.BasicPriceType);
    }

    public void readFromParcel(Parcel source) {
        this.BranchISN = source.readInt();
        this.PricesType_ISN = source.readInt();
        this.PricesTypeISNBranch = source.readInt();
        this.PricesTypeName = source.readString();
        this.Type = source.readString();
        this.BasicPriceType = source.readInt();
    }

    public PriceTypeData() {
    }

    protected PriceTypeData(Parcel in) {
        this.BranchISN = in.readInt();
        this.PricesType_ISN = in.readInt();
        this.PricesTypeISNBranch = in.readInt();
        this.PricesTypeName = in.readString();
        this.Type = in.readString();
        this.BasicPriceType = in.readInt();
    }

    public PriceTypeData copy() {
        Parcel parcel = Parcel.obtain();
        writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return CREATOR.createFromParcel(parcel);
    }
    public static final Parcelable.Creator<PriceTypeData> CREATOR = new Parcelable.Creator<PriceTypeData>() {
        @Override
        public PriceTypeData createFromParcel(Parcel source) {
            return new PriceTypeData(source);
        }

        @Override
        public PriceTypeData[] newArray(int size) {
            return new PriceTypeData[size];
        }
    };
}
