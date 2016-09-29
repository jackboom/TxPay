package com.jszf.txsystem.bean;

/**
 * @author jacking
 *         Created at 2016/9/5 15:30 .
 */
public class RefundBean {

    private String merchantNo;
    private String refundNo;
    private String orderNo;
    private String txRefundNo;
    private String bankRefundNo;
    private String refundAmount;
    private String actualRefundAmount;
    private String curCode;
    private String refundTime;
    private String dealRefundTime;
    private String dealCode;
    private String dealMsg;
    private String sign;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTxRefundNo() {
        return txRefundNo;
    }

    public void setTxRefundNo(String txRefundNo) {
        this.txRefundNo = txRefundNo;
    }

    public String getBankRefundNo() {
        return bankRefundNo;
    }

    public void setBankRefundNo(String bankRefundNo) {
        this.bankRefundNo = bankRefundNo;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getActualRefundAmount() {
        return actualRefundAmount;
    }

    public void setActualRefundAmount(String actualRefundAmount) {
        this.actualRefundAmount = actualRefundAmount;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getDealRefundTime() {
        return dealRefundTime;
    }

    public void setDealRefundTime(String dealRefundTime) {
        this.dealRefundTime = dealRefundTime;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
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
}
