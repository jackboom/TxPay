package com.jszf.txsystem.bean;

import java.util.List;

/**
 * @author jacking
 *         Created at 2016/8/31 16:06 .
 */
public class MainBean {
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
        private SummaryInfo summaryInfo;
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

        public SummaryInfo getSummaryInfo() {
            return summaryInfo;
        }

        public void setSummaryInfo(SummaryInfo summaryInfo) {
            this.summaryInfo = summaryInfo;
        }

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }

        public static class SummaryInfo {
            private String payType_1;
            private String payType_2;
            private String payType_3;
            private String payType_9;

            public String getPayType_2() {
                return payType_2;
            }

            public void setPayType_2(String payType_2) {
                this.payType_2 = payType_2;
            }

            public String getPayType_1() {
                return payType_1;
            }

            public void setPayType_1(String mPayType_1) {
                payType_1 = mPayType_1;
            }

            public String getPayType_3() {
                return payType_3;
            }

            public void setPayType_3(String mPayType_3) {
                payType_3 = mPayType_3;
            }

            public String getPayType_9() {
                return payType_9;
            }

            public void setPayType_9(String mPayType_9) {
                payType_9 = mPayType_9;
            }
        }

        public static class Rows {
            private String orderTime;
            private String orderAmount;

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getOrderAmount() {
                return orderAmount;
            }

            public void setOrderAmount(String orderAmount) {
                this.orderAmount = orderAmount;
            }
        }
    }
}
