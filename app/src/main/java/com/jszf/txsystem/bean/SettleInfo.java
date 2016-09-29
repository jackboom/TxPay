package com.jszf.txsystem.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SettleInfo implements Serializable {
    private static final long serialVersionUID = 2138827728240230886L;
    private String inAmount;
    private String outAmount;
    private String allFee;
    private String setteleAmount;
    private String payTime;

    public String getInAmount() {
        return inAmount;
    }

    public void setInAmount(String mInAmount) {
        inAmount = mInAmount;
    }

    public String getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(String mOutAmount) {
        outAmount = mOutAmount;
    }

    public String getAllFee() {
        return allFee;
    }

    public void setAllFee(String mAllFee) {
        allFee = mAllFee;
    }

    public String getSetteleAmount() {
        return setteleAmount;
    }

    public void setSetteleAmount(String mSetteleAmount) {
        setteleAmount = mSetteleAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String mPayTime) {
        payTime = mPayTime;
    }
}
