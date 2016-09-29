package com.jszf.txsystem.bean;

/**
 * @author jacking
 *         Created at 2016/8/31 15:40 .
 */
public class LoginBean {
    private String dealMsg;
    private String sign;
    private String mid;
    private String dealCode;
    private String MD5key;
    private String merchantNo;
    private String merchantName;

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

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    public String getMD5key() {
        return MD5key;
    }

    public void setMD5key(String MD5key) {
        this.MD5key = MD5key;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
