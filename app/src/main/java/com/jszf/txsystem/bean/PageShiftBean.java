package com.jszf.txsystem.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/13 10:28 .
 */
public class PageShiftBean extends TextBean implements Serializable {


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

    public static class Data implements Serializable{
        private String StartTime;
        private String EndTime;
        private String UserName;
        private double Recive;
        private String MerchantNo;
        private double Refund;
        private OrderInfo OrderInfo;
        private String Id;
        private List<Infos> Infos;

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
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

        public double getRecive() {
            return Recive;
        }

        public void setRecive(int Recive) {
            this.Recive = Recive;
        }

        public double getRefund() {
            return Refund;
        }

        public void setRefund(int Refund) {
            this.Refund = Refund;
        }

        public OrderInfo getOrderInfo() {
            return OrderInfo;
        }

        public void setOrderInfo(OrderInfo OrderInfo) {
            this.OrderInfo = OrderInfo;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public List<Infos> getInfos() {
            return Infos;
        }

        public void setInfos(List<Infos> Infos) {
            this.Infos = Infos;
        }

        public static class OrderInfo {
            private List<?> Orders;
            private List<?> Refunds;

            public List<?> getOrders() {
                return Orders;
            }

            public void setOrders(List<?> Orders) {
                this.Orders = Orders;
            }

            public List<?> getRefunds() {
                return Refunds;
            }

            public void setRefunds(List<?> Refunds) {
                this.Refunds = Refunds;
            }
        }

        public static class Infos {
            private String Name;
            private double Value;
            private int Type;

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public double getValue() {
                return Value;
            }

            public void setValue(int Value) {
                this.Value = Value;
            }

            public int getType() {
                return Type;
            }

            public void setType(int Type) {
                this.Type = Type;
            }
        }
    }
}