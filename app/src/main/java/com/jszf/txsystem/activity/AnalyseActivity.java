package com.jszf.txsystem.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.ScoreBean;
import com.jszf.txsystem.core.mvp.base.BaseActivity;
import com.jszf.txsystem.shadowviewhelper.ShadowProperty;
import com.jszf.txsystem.shadowviewhelper.ShadowViewHelper;
import com.jszf.txsystem.ui.ColumnChart;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.util.AcquireTimeNode;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.LineChartUtils2;
import com.jszf.txsystem.util.MD5Utils;
import com.jszf.txsystem.util.ParaUtils;
import com.jszf.txsystem.view.CustomTextView;
import com.jszf.txsystem.view.ViewPagerCompat;
import com.jszf.txsystem.view.wheelview.JudgeDate;
import com.jszf.txsystem.view.wheelview.ScreenInfo;
import com.jszf.txsystem.view.wheelview.WheelMain;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


public class AnalyseActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    @BindView(R.id.analyse_text_damount)
    TextView mAnalyseTextDamount;
    @BindView(R.id.analyse_text_mamount)
    TextView mAnalyseTextMamount;
    @BindView(R.id.iv_analyse_back)
    ImageView mIvAnalyseBack;
    @BindView(R.id.iv_analyse_more)
    ImageView mIvAnalyseMore;
    @BindView(R.id.tv_show_date)
    CustomTextView mTvShowDate;
    @BindView(R.id.rl_whole_touch)
    RelativeLayout mRlWholeTouch;
    @BindView(R.id.tv_month_amount)
    CustomTextView mTvMonthAmount;
    @BindView(R.id.tv_show_timeslot)
    CustomTextView mTvShowTimeslot;
    @BindView(R.id.analyse_ll_barchat)
    RelativeLayout mAnalyseLlBarchat;
    @BindView(R.id.linear)
    LinearLayout mLinear;
    private String startTime;
    private String endTime;
    private String startBarTime;
    private String endBarTime;
    private String orderTime;   //订单时间-天
    private String orderAmount; //订单时间(天)的交易金额
    private AlertDialog.Builder mBuilder;
    private int amountAll;   //总交易额
    private String amountWixin; //微信交易额
    private String amountAlipay; //支付宝交易额
    private String amountBdqb; //百度钱包交易额
    private String amountYzf; //翼支付交易额
    private Handler mHandler = new Handler();
    private int[] mTypeArr;
    private GestureDetector detector = new GestureDetector(this);
    // 限制最小移动像素
    private int FLING_MIN_DISTANCE = 100;
    private int start = 6;
    private int end = 0;
    private String mTotal;
    private String amountPos;
    private int[] weeArr;
    private int mIntAmountAli;
    private int mIntAmountWeixin;
    private int mIntAmountYzf;
    private int mIntAmountWeiPos;
    private int mIntAmountBdqb;
    private TextView[] tvDayArr = new TextView[7];
    private TextView[] tvWeekArr = new TextView[7];
    private List<Integer> weekList = new ArrayList<>();
    private WheelMain wheelMain;
    private String[] payTypeArr = new String[]{"微信", "支付宝", "翼支付", "百度钱包", "POS"};
    List<String> list = new ArrayList<>();
    List<String> barList = new ArrayList<>();
    List<HashMap<String, String>> listTimeData = new ArrayList<>();
    private int year;
    private int month;
    private ViewPagerCompat mViewpager;
    private List<View> mViewList = new ArrayList<>();
    private List<List<Float>> floatDataList = new ArrayList<>();
    private List<String[]> dateList = new ArrayList<>();
    private String mMonthDate;
    private int mMonth2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse2);
        ButterKnife.bind(this);
        initView();
        initTime(null);
        showLoadingDialog();
        requestBarchartData();
        requestDealData();
    }

    /**
     * 初始化时间数据等
     */
    private void initTime(String month) {
        list.clear();
        barList.clear();
        if (TextUtils.isEmpty(month)) {
            list = AcquireTimeNode.getTimeInitData();
            barList = AcquireTimeNode.getBarTimeInitData();
        } else {
            list = AcquireTimeNode.setTimeDataByMonth(month);
            barList = AcquireTimeNode.setBarTimeDataByMonth(month);
        }
        startTime = list.get(0);
        endTime = list.get(list.size() - 1);
        startBarTime = barList.get(0);
        endBarTime = barList.get(barList.size() - 1);

        String[] arr = endTime.split("-");
        mTvShowDate.setText(arr[0] + "-" + arr[1]);
        mTvShowTimeslot.setText(arr[1] + ".01-" + arr[1] + "." + arr[2]);
        String currentStartMonth = startTime.split("-")[1];
        Log.d("AnalyseActivity", "startTime" + startTime + ",endTime:" + endTime + ",list:" +
                list.size() + "currentStartMonth:" + currentStartMonth);
    }

    private void initShowDate() {
        floatDataList.clear();
        dateList.clear();
        weekList.clear();
        //界面个数
        int length = list.size() % 7 == 0 ? list.size() / 7 : list.size() / 7 + 1;
        for (int k = 0; k < length; k++) {
            List<Float> lists = new ArrayList<Float>();//线性图  范围10-100
            String[] dateArr = new String[7];
            String[] dayArr = new String[7];
            weeArr = new int[7];
            for (int i = 0; i < 7; i++) {
                lists.add((float) Integer.parseInt(listTimeData.get(i + (7 * k)).get("orderAmount")) / 100);
                dateArr = list.get(i + (7 * k)).split("-");
                String date = dateArr[2];
                Calendar c = Calendar.getInstance();
                String str = "";
                if (dateArr[1].startsWith("0")) {
                    str = dateArr[1].substring(1, 2);
                } else {
                    str = dateArr[1];
                }
                dayArr[i] = date;
                if (k == 0) {
                    c.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
                    c.set(Calendar.MONTH, Integer.parseInt(str) - 1);
                    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[2]));
                    int weekDay = c.get(Calendar.DAY_OF_WEEK);
                    if (weekDay > 7) {
                        weekList.add((weekDay++) - 7 * (k + 1));
                    } else {
                        weekList.add(weekDay++);
                    }
                }
            }
            lists.add(0, 0.00f);
            lists.add(lists.size(), 0.00f);
            floatDataList.add(lists);
            dateList.add(dayArr);
        }

    }


    private void requestBarchartData() {
        OkHttpUtils.post()
                .url(Constant.PATH)
                .params(getBarRequestParams())
                .build()
                .connTimeOut(10000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissLoading();
                        Log.d("AnalyseActivity", e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(Call call, Object o) {
                        try {
                            dismissLoading();
                            String result = o.toString();
                            JSONObject jsonObject = new JSONObject(result);
                            String data = jsonObject.getString("data");
                            if (TextUtils.isEmpty(data)) {
                                return;
                            }

                            JSONObject mJSONObject = new JSONObject(data);
                            String summaryInfo = mJSONObject.getString("summaryInfo");
                            Log.d("AnalyseActivity", "summaryInfo:" + summaryInfo);
                            JSONObject mSumJsonObject = new JSONObject(summaryInfo);
                            if (mSumJsonObject.has("payType_1")) {
                                amountAlipay = (Double.parseDouble(mSumJsonObject.getString("payType_1")) / 100) + "";
                                if (!TextUtils.isEmpty(mSumJsonObject.getString("payType_1"))) {
                                    mIntAmountAli = Integer.parseInt(mSumJsonObject.getString("payType_1"));
                                } else {
                                    mIntAmountAli = 0;
                                }
                            } else {
                                amountAlipay = "0.00";
                                mIntAmountAli = 0;
                            }
                            if (mSumJsonObject.has("payType_2")) {
                                amountWixin = (Double.parseDouble(mSumJsonObject.getString("payType_2")) / 100) + "";
                                if (!TextUtils.isEmpty(mSumJsonObject.getString("payType_2"))) {
                                    mIntAmountWeixin = Integer.parseInt(mSumJsonObject.getString("payType_2"));
                                } else {
                                    mIntAmountWeixin = 0;
                                }
                            } else {
                                amountWixin = "0.00";
                                mIntAmountWeixin = 0;
                            }
                            if (mSumJsonObject.has("payType_3")) {
                                amountBdqb = (Double.parseDouble(mSumJsonObject.getString("payType_3")) / 100) + "";
                                if (!TextUtils.isEmpty(mSumJsonObject.getString("payType_3"))) {
                                    mIntAmountBdqb = Integer.parseInt(mSumJsonObject.getString("payType_3"));
                                } else {
                                    mIntAmountBdqb = 0;
                                }
                            } else {
                                amountBdqb = "0.00";
                                mIntAmountBdqb = 0;
                            }
                            if (mSumJsonObject.has("payType_5")) {
                                amountPos = (Double.parseDouble(mSumJsonObject.getString("payType_5")) / 100 + "");
                                if (!TextUtils.isEmpty(mSumJsonObject.getString("payType_5"))) {
                                    mIntAmountWeiPos = Integer.parseInt(mSumJsonObject.getString("payType_5"));
                                } else {
                                    mIntAmountWeiPos = 0;
                                }
                            } else {
                                amountPos = "0.00";
                                mIntAmountWeiPos = 0;
                            }
                            if (mSumJsonObject.has("payType_9")) {
                                amountYzf = (Double.parseDouble(mSumJsonObject.getString("payType_9")) / 100) + "";
                                if (!TextUtils.isEmpty(mSumJsonObject.getString("payType_9"))) {
                                    mIntAmountYzf = Integer.parseInt(mSumJsonObject.getString("payType_9"));
                                } else {
                                    mIntAmountYzf = 0;
                                }
                            } else {
                                amountYzf = "0.00";
                                mIntAmountYzf = 0;
                            }
                            amountAll = mIntAmountAli + mIntAmountBdqb + mIntAmountWeiPos + mIntAmountWeixin + mIntAmountYzf;
                            Log.d("AnalyseActivity", "amountAll:" + amountAll + ",mIntAmountAli:" + mIntAmountAli + ",mIntAmountWeixin:" + mIntAmountWeixin);
                            mTypeArr = new int[]{mIntAmountWeixin, mIntAmountAli, mIntAmountYzf, mIntAmountBdqb, mIntAmountWeiPos};
                            mHandler.post(() -> setBarChartData());

                        } catch (Exception e) {

                        }
                    }
                });
    }

    @NonNull
    private HashMap<String, String> getBarRequestParams() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "offBatchSearchOrder");
        map.put("version", "V1.0");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("payType", "1,2,3,5,9");
        map.put("payMethod", "1");
        map.put("page", "1");
        map.put("status", "1");
        map.put("startDate", startBarTime);
        map.put("endDate", endBarTime);
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        return map;
    }


    /**
     * 网络请求图表数据
     */
    private void requestDealData() {
        final HashMap<String, String> map = getDealRequestParams();
        OkHttpUtils.post()
                .url(Constant.PATH)
                .params(getBarRequestParams())
                .build()
                .connTimeOut(10000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissLoading();
                        Log.d("AnalyseActivity", e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(Call call, Object o) {
                        try {
                            dismissLoading();
                            String result = o.toString();
                            if (result == null) {
                                return;
                            }
                            JSONObject jsonObject = new JSONObject(result);
                            String data = jsonObject.getString("data");
                            if (TextUtils.isEmpty(data)) {
                                return;
                            }
                            getChartData(data);
                            if (mTotal.equals("0")) {
                                mHandler.post(() -> {
                                    start -= 7;
                                    end -= 7;
                                    setDialog("没有数据记录!");
                                });
                                return;
                            }

                            mHandler.post(() -> {
                                mViewList.clear();
                                for (int m = 0; m < dateList.size(); m++) {
                                    View view = getLayoutInflater().inflate(R.layout.item_analyse_show, null);
                                    LineChart mLineChart = (LineChart) view.findViewById(R.id.lineChart);
                                    for (int j = 0; j < 7; j++) {
                                        int id = getResources().getIdentifier("tv_analyse_day" + j, "id", getApplicationInfo().packageName);
                                        int id2 = getResources().getIdentifier("tv_analyse_week" + j, "id", getApplicationInfo().packageName);
                                        TextView tv = (TextView) view.findViewById(id);
                                        TextView tv2 = (TextView) view.findViewById(id2);
                                        tvDayArr[j] = tv;
                                        tvWeekArr[j] = tv2;
                                    }
                                    for (int i = 0; i < 7; i++) {
                                        Log.d("Analyse", "date:" + dateList.get(m)[i]);
                                        tvDayArr[i].setText(dateList.get(m)[i]);
                                        int weekDay = weekList.get(i);
                                        String str = "";
                                        switch (weekDay) {
                                            case 1:
                                                str = "SUN";
                                                break;
                                            case 2:
                                                str = "MON";
                                                break;
                                            case 3:
                                                str = "TUE";
                                                break;
                                            case 4:
                                                str = "WED";
                                                break;
                                            case 5:
                                                str = "THU";
                                                break;
                                            case 6:
                                                str = "FRI";
                                                break;
                                            case 7:
                                                str = "SAT";
                                                break;
                                        }
                                        tvWeekArr[i].setText(str);

                                    }
                                    LineChartUtils2.showChart(AnalyseActivity.this, mLineChart, floatDataList.get(m), 0);
                                    mViewList.add(view);
                                }

                                mViewpager.setAdapter(new PagerAdapter() {
                                    @Override
                                    public Object instantiateItem(ViewGroup container, int position) {

                                        container.addView(mViewList.get(position));
                                        return mViewList.get(position);
                                    }

                                    @Override
                                    public void destroyItem(ViewGroup container, int position,
                                                            Object object) {
                                        if (mViewList.size() > 1) {

                                            container.removeView(mViewList.get(position));
                                        }
                                    }

                                    @Override
                                    public boolean isViewFromObject(View view, Object object) {
                                        return view == object;
                                    }

                                    @Override
                                    public int getCount() {
                                        return mViewList.size();
                                    }
                                });
                                if (mMonth2 == month) {
                                    mViewpager.setCurrentItem(mViewList.size() - 1);
                                }
                            });
                        } catch (Exception e) {

                        }

                    }
                });
    }


    @NonNull
    private HashMap<String, String> getDealRequestParams() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("service", "offBatchSearchOrder");
        map.put("version", "V1.0");
        map.put("merchantNo", MyApplication.merchantNo);
        map.put("payType", "1,2,3,5,9");
        map.put("payMethod", "1");
        map.put("page", "1");
        map.put("status", "1");
        map.put("startDate", startTime);
        map.put("endDate", endTime);
        map.put("sign", MD5Utils.MD5(ParaUtils.createLinkString(map) + MyApplication.MD5key));
        return map;
    }

    private void setBarChartData() {
        mTvMonthAmount.setText((float) amountAll / 100 + "");
        List<ScoreBean> list = new ArrayList<>();
        //柱状图  范围10-100
        for (int i = 0; i < 5; i++) {
            ScoreBean s = new ScoreBean();
            s.pay_amount = (float) mTypeArr[i] / 100;
            s.score = (float) mTypeArr[i] / amountAll * 100;
            s.pay_type = payTypeArr[i];
            list.add(s);
        }
        mLinear.removeAllViews();
        mLinear.addView(new ColumnChart(AnalyseActivity.this, list));
        mLinear.invalidate();
    }

    private void getChartData(String data) {
        try {
            listTimeData.clear();
            JSONObject jsonObject = new JSONObject(data);
            mTotal = jsonObject.getString("total");
            String rows = jsonObject.getString("rows");
            JSONArray jsonArray = new JSONArray(rows);
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> saveMap = new HashMap<>();
                saveMap.put("time", list.get(i));
                saveMap.put("orderAmount", "0");
                listTimeData.add(saveMap);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                orderTime = jsonObject1.getString("orderTime");
                orderAmount = jsonObject1.getString("orderAmount");
                if (i == jsonArray.length() - 1) {
                    mMonthDate = orderTime.substring(5, 7);
                }
                for (int j = 0; j < listTimeData.size(); j++) {
                    if (listTimeData.get(j).get("time").equals(orderTime)) {
                        listTimeData.get(j).put("orderAmount", orderAmount);
                        continue;
                    }
                }
            }
            initShowDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        mViewpager = (ViewPagerCompat) findViewById(R.id.viewpager);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        mMonth2 = month;
        mRlWholeTouch.setOnTouchListener(this);
//        mTime = System.currentTimeMillis();
        mBuilder = new AlertDialog.Builder(this);
        // View A
        ShadowProperty shadowProperty = new ShadowProperty()
                .setShadowColor(getResources().getColor(R.color.analyse_shadow))
                .setShadowRadius(ABTextUtil.dip2px(this, 4));
        ShadowViewHelper.bindShadowHelper(shadowProperty, mAnalyseLlBarchat);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAnalyseLlBarchat.getLayoutParams();
        lp.leftMargin = -shadowProperty.getShadowOffset();
        lp.rightMargin = -shadowProperty.getShadowOffset();
//        lp.bottomMargin = -shadowProperty.getShadowOffset();
//        lp.topMargin = -shadowProperty.getShadowOffset();
        mAnalyseLlBarchat.setLayoutParams(lp);
    }


    private void setDialog(String message) {
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBuilder.create().dismiss();
            }
        });
        mBuilder.create().show();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
            // 向左滑动
//            initShowDate(2);
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE) {
            // 向右滑动
//            initShowDate(0);
            Log.d("TAG", "右");
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

    @OnClick({R.id.iv_analyse_back, R.id.iv_analyse_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_analyse_back:
                finish();
                break;
            case R.id.iv_analyse_more:
                monthSelect();
                break;
        }
    }

    private void monthSelect() {
        LayoutInflater inflater = LayoutInflater.from(AnalyseActivity.this);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        final View timepickerview = inflater.inflate(R.layout.timepicker,
                null);
        ScreenInfo screenInfo = new ScreenInfo(AnalyseActivity.this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        String time = mTvShowDate.getText().toString();
        if (JudgeDate.isDate(time, "yyyy-MM")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        wheelMain.initDateTimePicker2(year, month - 1);
        final MyAlertDialog dialog1 = new MyAlertDialog(AnalyseActivity.this)
                .builder();
        dialog1.dialog_marTop.setVisibility(View.GONE);
        dialog1.setView(timepickerview)
                .setTitle("选择月份")
                .setNegativeButton("取消", v -> {
                })
                .setPositiveButton("确定", v -> {
                    String getTime = wheelMain.getMonth();
                    Log.d("AnalyseActivity", "getTime:" + getTime);
                    int year2 = Integer.parseInt(getTime.substring(0, 4));
                    mMonth2 = Integer.parseInt(getTime.substring(5, getTime.length()));
                    if (year2 > year) {
                        showShortToast("请选择正确的日期");
                        return;
                    } else if (year2 == year) {
                        if (mMonth2 > month) {
                            showShortToast("请选择正确的日期");
                            return;
                        } else {
                            initTime(mMonth2 + "");
                            mViewpager.removeAllViews();
                            requestBarchartData();
                            requestDealData();
                            Log.d("AnalyseActivity", "month2:" + mMonth2);
                        }
                    } else {
                        initTime(mMonth2 + "");
                        mViewpager.removeAllViews();
                        requestBarchartData();
                        requestDealData();
                    }
                });
        dialog1.show();

    }

    protected void showShortToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
