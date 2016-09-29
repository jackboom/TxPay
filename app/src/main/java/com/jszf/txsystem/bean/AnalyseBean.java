package com.jszf.txsystem.bean;

import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/5 17:09 .
 */
public class AnalyseBean {

    private ContentData data;
    private String dealMsg;
    private String sign;
    private String dealCode;

    public ContentData getData() {
        return data;
    }

    public void setData(ContentData data) {
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

    public static class ContentData {
        private String totalPage;
        private String total;
        private List<Rows> rows;
        private SummaryInfo summaryInfo;

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }

        public SummaryInfo getSummaryInfo() {
            return summaryInfo;
        }

        public void setSummaryInfo(SummaryInfo summaryInfo) {
            this.summaryInfo = summaryInfo;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
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

        public static class SummaryInfo {
            private String payType_1;
            private String payType_2;
            private String payType_3;
            private String payType_5;
            private String payType_9;

            public String getPayType_1() {
                return payType_1;
            }

            public void setPayType_1(String payType_1) {
                this.payType_1 = payType_1;
            }

            public String getPayType_2() {
                return payType_2;
            }

            public void setPayType_2(String payType_2) {
                this.payType_2 = payType_2;
            }

            public String getPayType_3() {
                return payType_3;
            }

            public void setPayType_3(String payType_3) {
                this.payType_3 = payType_3;
            }

            public String getPayType_5() {
                return payType_5;
            }

            public void setPayType_5(String payType_5) {
                this.payType_5 = payType_5;
            }

            public String getPayType_9() {
                return payType_9;
            }

            public void setPayType_9(String payType_9) {
                this.payType_9 = payType_9;
            }
        }
    }
}
