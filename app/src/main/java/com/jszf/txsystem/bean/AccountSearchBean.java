package com.jszf.txsystem.bean;

import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/1 14:06 .
 */
public class AccountSearchBean extends TextBean {

    private String total;
    private String dealMsg;
    private String sign;
    private String dealCode;
    private String merchantNo;
    private List<SettleInfoList> settleInfoList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public List<SettleInfoList> getSettleInfoList() {
        return settleInfoList;
    }

    public void setSettleInfoList(List<SettleInfoList> settleInfoList) {
        this.settleInfoList = settleInfoList;
    }

    public static class SettleInfoList {
        private String inAmount;
        private String outAmout;
        private String allFee;
        private String setteleAmount;
        private String payTime;

        public String getInAmount() {
            return inAmount;
        }

        public void setInAmount(String inAmount) {
            this.inAmount = inAmount;
        }

        public String getOutAmout() {
            return outAmout;
        }

        public void setOutAmout(String outAmout) {
            this.outAmout = outAmout;
        }

        public String getAllFee() {
            return allFee;
        }

        public void setAllFee(String allFee) {
            this.allFee = allFee;
        }

        public String getSetteleAmount() {
            return setteleAmount;
        }

        public void setSetteleAmount(String setteleAmount) {
            this.setteleAmount = setteleAmount;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }
    }
}
