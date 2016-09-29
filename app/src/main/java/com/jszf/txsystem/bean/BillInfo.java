package com.jszf.txsystem.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/5.
 */
public class BillInfo implements Serializable {
    private static final long serialVersionUID = 8505358240190660516L;
    private String payState;     //订单支付状态
    private String payTime;     //支付时间
    private String payType;     //支付方式
    private String payAmount;   //订单金额
    private String txOrderNo;   //同兴订单号
    private String mOrderNo;    //商户订单号
    private String txRefundNo="";    //同兴退款流水号
    private String mRefundNo  = "";   // 商户退款流水号
    private String refundApplyTime ="";   // 退款申请时间
    private String refunEndTime="";   // 退款完成时间
    private String refundState ="";   // 退款状态
    private String payChannelCode;  //第三方通道
    private int refundCount; //退款次数

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }

    public String getPayChannelCode() {
        return payChannelCode;
    }

    public void setPayChannelCode(String mPayChannelCode) {
        payChannelCode = mPayChannelCode;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String mPayState) {
        payState = mPayState;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String mPayTime) {
        payTime = mPayTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String mPayType) {
        payType = mPayType;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String mPayAmount) {
        payAmount = mPayAmount;
    }

    public String getTxOrderNo() {
        return txOrderNo;
    }

    public void setTxOrderNo(String mTxOrderNo) {
        txOrderNo = mTxOrderNo;
    }

    public String getOrderNo() {
        return mOrderNo;
    }

    public void setOrderNo(String mOrderNo) {
        this.mOrderNo = mOrderNo;
    }

    public String getTxRefundNo() {
        return txRefundNo;
    }

    public void setTxRefundNo(String mTxRefundNo) {
        txRefundNo = mTxRefundNo;
    }

    public String getRefundNo() {
        return mRefundNo;
    }

    public void setRefundNo(String mRefundNo) {
        this.mRefundNo = mRefundNo;
    }

    public String getRefundApplyTime() {
        return refundApplyTime;
    }

    public void setRefundApplyTime(String mRefundApplyTime) {
        refundApplyTime = mRefundApplyTime;
    }

    public String getRefunEndTime() {
        return refunEndTime;
    }

    public void setRefunEndTime(String mRefunEndTime) {
        refunEndTime = mRefunEndTime;
    }

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String mRefundState) {
        refundState = mRefundState;
    }
}
