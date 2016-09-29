package com.jszf.txsystem.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/13 10:18 .
 */
public class PageOrderBean extends TextBean implements Serializable{

    private int TotalRecords;
    private int TotalPages;
    private int PageSize;
    private int PageIndex;
    private Object AddParameters;
    private Object MerchantNo;
    private Object MerchantKey;
    private boolean Success;
    private Object Msg;
    private String Guid;
    private String Sign;
    private List<Data> Data;

    public int getTotalRecords() {
        return TotalRecords;
    }

    public void setTotalRecords(int TotalRecords) {
        this.TotalRecords = TotalRecords;
    }

    public int getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(int TotalPages) {
        this.TotalPages = TotalPages;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int PageIndex) {
        this.PageIndex = PageIndex;
    }

    public Object getAddParameters() {
        return AddParameters;
    }

    public void setAddParameters(Object AddParameters) {
        this.AddParameters = AddParameters;
    }

    public Object getMerchantNo() {
        return MerchantNo;
    }

    public void setMerchantNo(Object MerchantNo) {
        this.MerchantNo = MerchantNo;
    }

    public Object getMerchantKey() {
        return MerchantKey;
    }

    public void setMerchantKey(Object MerchantKey) {
        this.MerchantKey = MerchantKey;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public Object getMsg() {
        return Msg;
    }

    public void setMsg(Object Msg) {
        this.Msg = Msg;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String Guid) {
        this.Guid = Guid;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String Sign) {
        this.Sign = Sign;
    }

    public List<Data> getData() {
        return Data;
    }

    public void setData(List<Data> Data) {
        this.Data = Data;
    }

    public static class Data {
        private String TxOrderNo;
        private String OrderNo;
        private int PayType;
        private double Amt;
        private String MerchantNo;
        private String UserName;
        private String ShopTime;
        private int OrderStatus;
        private String AuthCode;
        private String Id;

        public String getTxOrderNo() {
            return TxOrderNo;
        }

        public void setTxOrderNo(String TxOrderNo) {
            this.TxOrderNo = TxOrderNo;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public int getPayType() {
            return PayType;
        }

        public void setPayType(int PayType) {
            this.PayType = PayType;
        }

        public double getAmt() {
            return Amt;
        }

        public void setAmt(double Amt) {
            this.Amt = Amt;
        }

        public String getMerchantNo() {
            return MerchantNo;
        }

        public void setMerchantNo(String MerchantNo) {
            this.MerchantNo = MerchantNo;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getShopTime() {
            return ShopTime;
        }

        public void setShopTime(String ShopTime) {
            this.ShopTime = ShopTime;
        }

        public int getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(int OrderStatus) {
            this.OrderStatus = OrderStatus;
        }

        public String getAuthCode() {
            return AuthCode;
        }

        public void setAuthCode(String AuthCode) {
            this.AuthCode = AuthCode;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }
    }
}
