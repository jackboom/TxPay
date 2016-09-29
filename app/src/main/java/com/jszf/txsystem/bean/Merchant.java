package com.jszf.txsystem.bean;

import android.os.Parcelable;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class Merchant implements Serializable {
    private static final Merchant single = new Merchant();
    private static final long serialVersionUID = -5705984344829490193L;
    private String merchantNo;          //商户号
    private String MD5key;
    private String userLoginName;   //用户名
    private String merchantName;    //商户名
    private String bankName;        //开户银行
    private String bankCompanyName; //银行账户名
    private String bankCode;        //卡号
    private String bankAddress;     //开户支行
    private String settlementPeriodType;    //结算周期类型
    private String settlementPeriodValue;   //结算周期
    private String accountRetainAmount;     //账户余额
    private String accountFixAmount;        //不可用金额
    private String accountAllAmount;        //待结算金额
    private String accountUsableAmount;     //可用金额
    private String tradingStatus;           //交易状态
    private String settlementStatus;        //结算状态
    private String telphone;                //手机号
//    private String productRateList;         //产品费率列表
    private List<HomeBean.ProductRateList> productRateList;
    private ArrayList<Parcelable> productList;

    public ArrayList<Parcelable> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Parcelable> mProductList) {
        productList = mProductList;
    }

    public static synchronized Merchant getIntance() {
        return single;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String mMerchantNo) {
        merchantNo = mMerchantNo;
    }

    public String getMD5key() {
        return MD5key;
    }

    public void setMD5key(String mMD5key) {
        MD5key = mMD5key;
    }

    public static Merchant getSingle() {
        return single;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String mUserLoginName) {
        userLoginName = mUserLoginName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String mMerchantName) {
        merchantName = mMerchantName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String mBankName) {
        bankName = mBankName;
    }

    public String getBankCompanyName() {
        return bankCompanyName;
    }

    public void setBankCompanyName(String mBankCompanyName) {
        bankCompanyName = mBankCompanyName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String mBankCode) {
        bankCode = mBankCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String mBankAddress) {
        bankAddress = mBankAddress;
    }

    public String getSettlementPeriodType() {
        return settlementPeriodType;
    }

    public void setSettlementPeriodType(String mSettlementPeriodType) {
        settlementPeriodType = mSettlementPeriodType;
    }

    public String getSettlementPeriodValue() {
        return settlementPeriodValue;
    }

    public void setSettlementPeriodValue(String mSettlementPeriodValue) {
        settlementPeriodValue = mSettlementPeriodValue;
    }

    public String getAccountRetainAmount() {
        return accountRetainAmount;
    }

    public void setAccountRetainAmount(String mAccountRetainAmount) {
        accountRetainAmount = mAccountRetainAmount;
    }

    public String getAccountFixAmount() {
        return accountFixAmount;
    }

    public void setAccountFixAmount(String mAccountFixAmount) {
        accountFixAmount = mAccountFixAmount;
    }

    public String getAccountAllAmount() {
        return accountAllAmount;
    }

    public void setAccountAllAmount(String mAccountAllAmount) {
        accountAllAmount = mAccountAllAmount;
    }

    public String getAccountUsableAmount() {
        return accountUsableAmount;
    }

    public void setAccountUsableAmount(String mAccountUsableAmount) {
        accountUsableAmount = mAccountUsableAmount;
    }

    public String getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(String mTradingStatus) {
        tradingStatus = mTradingStatus;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String mSettlementStatus) {
        settlementStatus = mSettlementStatus;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String mTelphone) {
        telphone = mTelphone;
    }

//    public String getProductRateList() {
//        return productRateList;
//    }
//
//    public void setProductRateList(String mProductRateList) {
//        productRateList = mProductRateList;
//    }

    public List<HomeBean.ProductRateList> getProductRateList() {
        return productRateList;
    }

    public void setProductRateList(List<HomeBean.ProductRateList> mProductRateList) {
        productRateList = mProductRateList;
    }
}
