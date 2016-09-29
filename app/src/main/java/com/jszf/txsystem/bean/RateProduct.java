package com.jszf.txsystem.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/5/6.
 */
public class RateProduct implements Parcelable{

    private static final long serialVersionUID = 5160710546770341658L;
    private String productName;
    private String productId ;
    private String productRateType;
    private String rate;
    private String period;

    public RateProduct(Parcel in) {
        productName = in.readString();
        productId = in.readString();
        productRateType = in.readString();
        rate = in.readString();
        period = in.readString();
    }

    public static final Creator<RateProduct> CREATOR = new Creator<RateProduct>() {
        @Override
        public RateProduct createFromParcel(Parcel in) {
            return new RateProduct(in);
        }

        @Override
        public RateProduct[] newArray(int size) {
            return new RateProduct[size];
        }
    };

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String mPeriod) {
        period = mPeriod;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String mProductName) {
        productName = mProductName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String mProductId) {
        productId = mProductId;
    }

    public String getProductRateType() {
        return productRateType;
    }

    public void setProductRateType(String mProductRateType) {
        productRateType = mProductRateType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String mRate) {
        rate = mRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(productId);
        dest.writeString(productRateType);
        dest.writeString(rate);
        dest.writeString(period);
    }
}
