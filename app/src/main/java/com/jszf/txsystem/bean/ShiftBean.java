package com.jszf.txsystem.bean;

import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/12 9:38 .
 */
public class ShiftBean extends TextBean {
    private ShiftModel _ShiftModel;
    private Object MerchantNo;
    private Object MerchantKey;
    private boolean Success;
    private Object Msg;
    private String Guid;
    private String Sign;

    public ShiftModel get_ShiftModel() {
        return _ShiftModel;
    }

    public void set_ShiftModel(ShiftModel _ShiftModel) {
        this._ShiftModel = _ShiftModel;
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

    public static class ShiftModel {
        private String StartTime;
        private String EndTime;
        private String MerchantNo;
        private String UserName;
        private int Recive;
        private int Refund;
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

        public int getRecive() {
            return Recive;
        }

        public void setRecive(int Recive) {
            this.Recive = Recive;
        }

        public int getRefund() {
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
            private List<Orders> Orders;
            private List<?> Refunds;

            public List<Orders> getOrders() {
                return Orders;
            }

            public void setOrders(List<Orders> Orders) {
                this.Orders = Orders;
            }

            public List<?> getRefunds() {
                return Refunds;
            }

            public void setRefunds(List<?> Refunds) {
                this.Refunds = Refunds;
            }

            public static class Orders {
                private String Id;
                private Object TxOrderNo;
                private double Amt;
                private int OrderStatus;

                public String getId() {
                    return Id;
                }

                public void setId(String Id) {
                    this.Id = Id;
                }

                public Object getTxOrderNo() {
                    return TxOrderNo;
                }

                public void setTxOrderNo(Object TxOrderNo) {
                    this.TxOrderNo = TxOrderNo;
                }

                public double getAmt() {
                    return Amt;
                }

                public void setAmt(double Amt) {
                    this.Amt = Amt;
                }

                public int getOrderStatus() {
                    return OrderStatus;
                }

                public void setOrderStatus(int OrderStatus) {
                    this.OrderStatus = OrderStatus;
                }
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

            public void setValue(double Value) {
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
