package com.jszf.txsystem.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/27.
 */
public class OrderInfo implements Serializable{
    private static final long serialVersionUID = 6062022677018623320L;
    public static final OrderInfo single = new OrderInfo();
    private String orderNo;
    private String orderAmount;
    private String orderTime;
    private String orderTimestamp;
    private String productName;     //产品名
    private String productDesc;

    public OrderInfo(String mOrderNo, String mOrderAmount, String mOrderTime) {
        orderNo = mOrderNo;
        orderAmount = mOrderAmount;
        orderTime = mOrderTime;
    }

    public OrderInfo() {
        super();
    }

    public static synchronized OrderInfo getInstance(){
        return single;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String mOrderNo) {
        orderNo = mOrderNo;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String mOrderAmount) {
        orderAmount = mOrderAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String mOrderTime) {
        orderTime = mOrderTime;
    }

    public String getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(String mOrderTimestamp) {
        orderTimestamp = mOrderTimestamp;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String mProductName) {
        productName = mProductName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String mProductDesc) {
        productDesc = mProductDesc;
    }
}
