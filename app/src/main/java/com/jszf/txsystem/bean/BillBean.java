package com.jszf.txsystem.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/1 10:14 .
 */
public class BillBean extends TextBean implements Serializable{

    private Data data;
    private String dealMsg;
    private String sign;
    private String dealCode;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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

    public static class Data {
        private int totalPage;
        private int total;
        private List<Rows> rows;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }

        public static class Rows {
            private String txOrderNo;
            private String mOrderNo;
            private String orderAmount;
            private String status;
            private String payType;
            private String orderTime;
            private List<Refund> backs;

            public String getTxOrderNo() {
                return txOrderNo;
            }

            public void setTxOrderNo(String txOrderNo) {
                this.txOrderNo = txOrderNo;
            }

            public String getMOrderNo() {
                return mOrderNo;
            }

            public void setMOrderNo(String mOrderNo) {
                this.mOrderNo = mOrderNo;
            }

            public String getOrderAmount() {
                return orderAmount;
            }

            public void setOrderAmount(String orderAmount) {
                this.orderAmount = orderAmount;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public List<Refund> getBacks() {
                return backs;
            }


            public void setBacks(List<Refund> mBacks) {
                backs = mBacks;
            }

            public static class Refund {
                private String txRefundNo;
                private String mRefundNo;
                private String orderTime;
                private String txPayTime;
                private String status;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String mStatus) {
                    status = mStatus;
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

                public String getOrderTime() {
                    return orderTime;
                }

                public void setOrderTime(String mOrderTime) {
                    orderTime = mOrderTime;
                }

                public String getTxPayTime() {
                    return txPayTime;
                }

                public void setTxPayTime(String mTxPayTime) {
                    txPayTime = mTxPayTime;
                }
            }
        }
    }
}
