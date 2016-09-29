package com.jszf.txsystem.bean;

import java.util.List;

/**
 * @author jacking
 *         Created at 2016/8/31 14:52 .
 */
public class HomeBean extends TextBean {
    private String bankCode;
    private String bankCompanyName;
    private String accountAllAmount;
    private String dealMsg;
    private String sign;
    private String bankName;
    private String tradingStatus;
    private String bankAddress;
    private String accountFixAmount;
    private String settlementPeriodType;
    private String merchantName;
    private String accountRetainAmount;
    private String settlementPeriodValue;
    private String accountUsableAmount;
    private String settlementStatus;
    private String telphone;
    private String dealCode;
    private String merchantNo;
    private List<ProductRateList> productRateList;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCompanyName() {
        return bankCompanyName;
    }

    public void setBankCompanyName(String bankCompanyName) {
        this.bankCompanyName = bankCompanyName;
    }

    public String getAccountAllAmount() {
        return accountAllAmount;
    }

    public void setAccountAllAmount(String accountAllAmount) {
        this.accountAllAmount = accountAllAmount;
    }

    public String getDealMsg() {
        return dealMsg;
    }

    public void setDealMsg(String dealMsg) {
        this.dealMsg = dealMsg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(String tradingStatus) {
        this.tradingStatus = tradingStatus;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getAccountFixAmount() {
        return accountFixAmount;
    }

    public void setAccountFixAmount(String accountFixAmount) {
        this.accountFixAmount = accountFixAmount;
    }

    public String getSettlementPeriodType() {
        return settlementPeriodType;
    }

    public void setSettlementPeriodType(String settlementPeriodType) {
        this.settlementPeriodType = settlementPeriodType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAccountRetainAmount() {
        return accountRetainAmount;
    }

    public void setAccountRetainAmount(String accountRetainAmount) {
        this.accountRetainAmount = accountRetainAmount;
    }

    public String getSettlementPeriodValue() {
        return settlementPeriodValue;
    }

    public void setSettlementPeriodValue(String settlementPeriodValue) {
        this.settlementPeriodValue = settlementPeriodValue;
    }

    public String getAccountUsableAmount() {
        return accountUsableAmount;
    }

    public void setAccountUsableAmount(String accountUsableAmount) {
        this.accountUsableAmount = accountUsableAmount;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public List<ProductRateList> getProductRateList() {
        return productRateList;
    }

    public void setProductRateList(List<ProductRateList> productRateList) {
        this.productRateList = productRateList;
    }

    public static class ProductRateList {
        public String productId;
        public String productName;
        public String productRateType;
        public String rate;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductRateType() {
            return productRateType;
        }

        public void setProductRateType(String productRateType) {
            this.productRateType = productRateType;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }
}
