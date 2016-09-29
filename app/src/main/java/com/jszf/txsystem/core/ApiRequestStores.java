package com.jszf.txsystem.core;

import com.jszf.txsystem.bean.AccountSearchBean;
import com.jszf.txsystem.bean.AnalyseBean;
import com.jszf.txsystem.bean.BillBean;
import com.jszf.txsystem.bean.BillDetailBean;
import com.jszf.txsystem.bean.CaptureBean;
import com.jszf.txsystem.bean.HomeBean;
import com.jszf.txsystem.bean.LoginBean;
import com.jszf.txsystem.bean.MainBean;
import com.jszf.txsystem.bean.MessageBean;
import com.jszf.txsystem.bean.NoticeBean;
import com.jszf.txsystem.bean.OrderRefundBean;
import com.jszf.txsystem.bean.PageOrderBean;
import com.jszf.txsystem.bean.PageShiftBean;
import com.jszf.txsystem.bean.RefundBean;
import com.jszf.txsystem.bean.ResetPasswordBean;
import com.jszf.txsystem.bean.ShiftBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author jacking
 *         Created at 2016/8/29 10:33 .
 *         网络请求(结合mvp)/okhttputils
 */
public interface ApiRequestStores {
//    public static final String PATH = "http://api.tongxingpay.com/txpayApi/app?";
    public static final String BASE_URL = "http://api.tongxingpay.com/";
    public static final String BASE_URL2 = "http://pc.tongxingpay.com/";
//    public static final String BASE_URL3 = "http://192.168.1.110:1463/";
//    public static final String SHIFT_CREATE_URL= "http://pc.tongxingpay.com/";


    //登录请求
    @POST("txpayApi/app?")
    Observable<LoginBean> login(@QueryMap HashMap<String,String> params);

    //home页面请求
    @GET("txpayApi/app?")
    Observable<HomeBean> requestForHome(@QueryMap HashMap<String,String> params);

    //fragment首页请求
    @GET("txpayApi/app?")
    Observable<MainBean> requestForMain(@QueryMap HashMap<String,String> params);

    //notice公告请求
    @GET("txpayApi/app?")
    Observable<NoticeBean> requestForNotice(@QueryMap HashMap<String,String> params);

    //账单请求
    @GET("txpayApi/app?")
    Observable<BillBean> requestForBillinfo(@QueryMap HashMap<String,String> params);

    //到账查询请求
    @GET("txpayApi/app?")
    Observable<AccountSearchBean> requestForAccountSearch(@QueryMap HashMap<String,String> params);

    //修改密码
    @GET("txpayApi/app?")
    Observable<ResetPasswordBean> requestResetPassword(@QueryMap HashMap<String,String> params);

    //退款
    @GET("txpayApi/app?")
    Observable<RefundBean> requestRefund(@QueryMap HashMap<String,String> params);

    //发送验证短信
    @GET("txpayApi/app?")
    Observable<MessageBean> requestSendMessage(@QueryMap HashMap<String,String> params);

    //详情账单信息
    @GET("txpayApi/app?")
    Observable<BillDetailBean> requestBillDetail(@QueryMap HashMap<String,String> params);

    //扫描条码
    @GET("txpayApi/app?")
    Observable<CaptureBean> requestCapture2(@QueryMap HashMap<String,String> params);

    //统计分析请求
    @GET("txpayApi/app?")
    Observable<AnalyseBean> requestAnalyse(@QueryMap HashMap<String,String> params);

     //扫描条码
//     @FormUrlEncoded
     @GET("order/new?")
    Observable<CaptureBean> requestCapture(@QueryMap HashMap<String,String> params);

    //订单分页数据
    @FormUrlEncoded
    @POST("order/grid?")
    Observable<PageOrderBean>  requestPageOrder(@FieldMap HashMap<String,String> params);

    //订单退款
    @FormUrlEncoded
    @POST("order/refund?")
    Observable<OrderRefundBean> requestOrderRefund(@FieldMap HashMap<String,String> params);

    //创建交班扎帐
    @FormUrlEncoded
    @POST("shift/new?")
    Observable<ShiftBean> requestShift(@FieldMap HashMap<String,String> params);

    //交班扎帐分页数据
    @FormUrlEncoded
    @POST("shift/grid?")
    Observable<PageShiftBean>  requestPageShift(@FieldMap HashMap<String,String> params);
}

//http://api.tongxingpay.com/txpayApi/app?&Cp=1&
// Guid=5ae38a7d-6e0b-4a29-b6b8-b226e6e26675&MerchantKey
// =dea4c02423794a8ead4743b10a79126f&Size=10&MerchantNo=TX0000039
// &RecordsType=%E5%95%86%E6%88%B7%E6%89%80%E6%9C%89&
// Sign=Dp0s8jcS%2BopJhc4Rzforv3FKKmYK80rqu17lonF7dXf/UYAKZ%2Bm11lUk46
// xAPK8uO5VvhWevmLn1PcAI5fR2WEEUPFgouXSi/PMEoZ57O2EqmCtEHtPlBtmfD
// %2BwgJHCXkolgZfvH7wUhH7kcE5h4Flq9GMIeDMxMockHELNL1VQ%3D