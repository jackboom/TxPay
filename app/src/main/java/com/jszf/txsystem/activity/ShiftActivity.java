package com.jszf.txsystem.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.adapter.ExpandableShiftAdapter;
import com.jszf.txsystem.adapter.ShiftInfoAdapter;
import com.jszf.txsystem.bean.PageShiftBean;
import com.jszf.txsystem.bean.ShiftBean;
import com.jszf.txsystem.bean.ShiftItemInfo;
import com.jszf.txsystem.bean.ShiftSimpleBean;
import com.jszf.txsystem.core.ApiRequestStores;
import com.jszf.txsystem.core.HttpHelper;
import com.jszf.txsystem.core.mvp.MvpActivity;
import com.jszf.txsystem.core.mvp.shift.IShiftView;
import com.jszf.txsystem.core.mvp.shift.ShiftPresenterImpl;
import com.jszf.txsystem.service_receiver.BluetoothService;
import com.jszf.txsystem.ui.MyAlertDialog;
import com.jszf.txsystem.ui.PullToRefreshLayout;
import com.jszf.txsystem.ui.PullableExpandableListView;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.GuidUtils;
import com.jszf.txsystem.util.PrintUtils;
import com.jszf.txsystem.util.RSAUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ShiftActivity extends MvpActivity<IShiftView, ShiftPresenterImpl> implements IShiftView {

    @BindView(R.id.iv_shift_back)
    ImageView mIvShiftBack;
    @BindView(R.id.tv_shift_submit)
    TextView mTvShiftSubmit;
    @BindView(R.id.refresh_expand)
    PullableExpandableListView mRefreshExpand;
    @BindView(R.id.refreshing_pull)
    PullToRefreshLayout mRefreshingPull;
    @BindView(R.id.rl_shift_empty)
    RelativeLayout mRlShiftEmpty;
    @BindView(R.id.tv_shift_start)
    TextView mTvShiftStart;
    @BindView(R.id.tv_shift_end)
    TextView mTvShiftEnd;
    private int page = 1;
    private int totalPage;
    private ShiftInfoAdapter mAdapter;
    private RecyclerAdapterWithHF mAdapterWithHF;
    private Handler handler = new Handler();

    private String startTime = "";
    private String endTime = "";
    private float paddingDip = 8;
    private float verticalSpaceDip = 8;
    private LinearLayout container;
    private View linearView;
    private ExpandableShiftAdapter adapter;
    private boolean isAnd = false;
    private boolean noUpdate = false;
    private int pageIndex;
    private int[] typeArr = {R.drawable.icon_shift_all, R.drawable.icon_shift_success, R.drawable.icon_shift_failed, R.drawable.icon_shift_wait,
            R.drawable.icon_shift_success, R.drawable.icon_shift_in_refund, R.drawable.icon_shift_failed};
    private String[] stateArr = {"总订单", "交易成功", "交易失败", "待审核", "退款成功", "退款中", "退款失败"};
    private int totalClass;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
        ButterKnife.bind(this);
        setStatusBar();
        init();
        setAdapter();
        initRefreshLoadMore();
        initExpandableListView();
        requestPageShift();
    }

    private void init() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = format.format(date);
        endTime = endTime.substring(5, endTime.length() - 3);
    }

    private void initRefreshLoadMore() {
        mRefreshingPull.setOnRefreshListener(new MyListener());

    }

    /**
     * ExpandableListView初始化方法
     */
    private void initExpandableListView() {
        //设置 属性 GroupIndicator 去掉默认向下的箭头
        mRefreshExpand.setGroupIndicator(null);
//        mRefreshExpand.setDivider(null);
        adapter = new ExpandableShiftAdapter(this);
        mRefreshExpand.setAdapter(adapter);
        mRefreshExpand.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (parent.isGroupExpanded(groupPosition)) {
                // 如果展开则关闭
                parent.collapseGroup(groupPosition);

            } else {
                // 如果关闭则打开，注意这里是手动打开不要默认滚动否则会有bug
                parent.expandGroup(groupPosition);
            }
            return true;
        });
    }

    public static String timeStampDate(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    private void productShift() {
        showLoadingDialog();
        final ApiRequestStores apiStores = HttpHelper.getInstance().getRetrofit2().create(ApiRequestStores.class);
        Observable<ShiftBean> shiftBeanObservable = apiStores.requestShift(getHashMap());
        shiftBeanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShiftBean>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        Log.d("ShiftActivity", "e:" + e);
                    }

                    @Override
                    public void onNext(ShiftBean shiftBean) {
                        dismissLoading();
                        if (shiftBean.isSuccess()) {
                            Log.d("ShiftActivity", "扎帐成功!");
                            handler.postDelayed(() -> setDialog("扎帐成功!", shiftBean), 500);
                        }
                    }
                });
    }


    private void setAdapter() {
        mRlShiftEmpty.setVisibility(View.GONE);
        mRefreshingPull.setVisibility(View.VISIBLE);
    }


    private void requestPageShift() {
        mvpPresenter.requestShiftInfo(getRequestParams(mvpPresenter.getStartIndex()));
    }

    @Override
    protected void initData() {
    }

    @Override
    protected ShiftPresenterImpl createPresenter() {
        return new ShiftPresenterImpl(this);
    }

    /**
     * 交班扎帐
     * 获取请求参数
     *
     * @return HashMap
     */
    private HashMap<String, String> getHashMap() {
        HashMap<String, String> map = new HashMap<>();
        String guid = GuidUtils.getVarUUID();
        map.put("Guid", guid);
        map.put("MerchantNo", MyApplication.merchantNo);
        map.put("MerchantKey", MyApplication.MD5key);
        map.put("UserName", MyApplication.userLoginName);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        startTime = sdf.format(date);
        Log.d("ShiftActivity", "shit:time----->" + startTime);
        map.put("StartTime", startTime);
        try {
            String sign = RSAUtils.sign(guid, Constant.AppPrivateKey);
            map.put("Sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 交班扎帐分页
     * 获取请求参数
     *
     * @return HashMap
     */
    private HashMap<String, String> getRequestParams(int page) {
        HashMap<String, String> map = new HashMap<>();
        String guid = GuidUtils.getVarUUID();
        map.put("Guid", guid);
        map.put("MerchantNo", MyApplication.merchantNo);
        map.put("Cp", page + "");
        map.put("Size", "10");
//        map.put("Sort", "10");
//        map.put("Order", "10");
        map.put("MerchantKey", MyApplication.MD5key);
        map.put("UserName", MyApplication.userLoginName);
        try {
            String sign = RSAUtils.sign(guid, Constant.AppPrivateKey);
            map.put("Sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void onShiftRequest(PageShiftBean pageShiftBean) {
        if (pageShiftBean.isSuccess()) {
            Log.d("ShiftActivity", "-------->SUCCESS---");
            totalPage = pageShiftBean.getTotalPages();
            pageIndex = pageShiftBean.getPageIndex();
            totalClass = pageShiftBean.getTotalRecords();

            Log.d("ShiftActivity", "totalPage:" + totalPage);
            List<PageShiftBean.Data> dataList = pageShiftBean.getData();
            List<ShiftSimpleBean> shiftList = new ArrayList<>();
            List<PageShiftBean.Data> mGroup = new ArrayList<>();        // 组名
            HashMap<Integer, List<ShiftItemInfo>> child = new HashMap<>();

            if (dataList == null || dataList.size() == 0) {
                if (isAnd) {
                    noUpdate = true;

                    mRlShiftEmpty.setVisibility(View.GONE);
                    mRefreshingPull.setVisibility(View.VISIBLE);
                } else {
                    mRlShiftEmpty.setVisibility(View.VISIBLE);
                    mRefreshingPull.setVisibility(View.GONE);
                    return;
                }
            } else {
                mRlShiftEmpty.setVisibility(View.GONE);
                mRefreshingPull.setVisibility(View.VISIBLE);
            }
            if (noUpdate) {
                Log.d("ShiftActivity", "noUpdate:" + noUpdate);
                return;
            }
            for (int i = 0; i < dataList.size(); i++) {
//                    mGroup.add(dataList.get(i));
                //重新构建实体类
                PageShiftBean.Data data = dataList.get(i);
                List<PageShiftBean.Data.Infos> infos = data.getInfos();
                ShiftSimpleBean shiftSimpleBean = new ShiftSimpleBean();
                String classId = "";
                if (pageIndex == 1) {
                    classId = (totalClass - i) + "";
                } else {
                    classId = totalClass - ((pageIndex - 1) * 10 + i) + "";
                }
                shiftSimpleBean.setClassId(classId);
                shiftSimpleBean.setStartTime(data.getStartTime());
                shiftSimpleBean.setEndTime(data.getEndTime());
                shiftSimpleBean.setRecive(data.getRecive());
                shiftSimpleBean.setUserName(data.getUserName());
                changeInfos(shiftSimpleBean, child, i, infos);
                shiftList.add(shiftSimpleBean);
            }
            Log.d("ExpandableShiftAdapter", "child.size():" + child.size());
            try {
                Log.d("ShiftActivity", "mGroup-------------" + mGroup.get(0).getEndTime());
                Log.d("ShiftActivity", "mGroup.size():" + mGroup.size());
            } catch (Exception e) {
                Log.d("ShiftActivity", "e:" + e);
            }
            if (noUpdate) {
                Log.d("BillActivity", "noUpdate:" + noUpdate);
                return;
            }
            if (page <= totalPage) {
                if (page == mvpPresenter.getStartIndex()) {
                    startTime = dataList.get(0).getEndTime();
                    String start = startTime.substring(5, startTime.length() - 3);
                    start = start.replace("/", "-");
                    mTvShiftStart.setText(start);
                    mTvShiftEnd.setText(endTime);
                    adapter.setDatas(shiftList);
                } else {
//                    adapter.addData(mGroup, child);
                    adapter.addDatas(shiftList);
                }
            }
        }
    }

    private void changeInfos(ShiftSimpleBean shiftSimpleBean, HashMap<Integer, List<ShiftItemInfo>> mChildren, int position, List<PageShiftBean.Data.Infos> infos) {
        List<ShiftItemInfo> list1 = new ArrayList<>();
        List<ShiftSimpleBean.Child> list = new ArrayList<>();
        List<String> amountList = new ArrayList<>();
        List<String> countList = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            if (i % 2 == 0) {
                amountList.add(infos.get(i).getValue() + "");
            } else {
                countList.add((int) infos.get(i).getValue() + "");
            }
        }
        for (int i = 0; i < amountList.size(); i++) {
//            ShiftItemInfo shiftItemInfo = new ShiftItemInfo();
//            shiftItemInfo.setShiftAmount(amountList.get(i));
//            shiftItemInfo.setShiftCount(countList.get(i));
//            list1.add(shiftItemInfo);
            ShiftSimpleBean.Child child = new ShiftSimpleBean.Child();
            child.setShiftAmount(amountList.get(i));
            child.setShiftCount(countList.get(i));
            child.setShiftType(typeArr[i]);
            child.setShiftState(stateArr[i]);
            list.add(child);
        }
        shiftSimpleBean.setChildList(list);
    }


    private void setDialog(String message, ShiftBean shiftBean) {

        final MyAlertDialog dialog1 = new MyAlertDialog(ShiftActivity.this)
                .builder();
        dialog1.dialog_marTop.setVisibility(View.GONE);
        dialog1.setMsg(message)
                .setNegativeButton("确定", v -> {
//                    PrintUtils.printShitf(ShiftActivity.this, shiftBean);
                    mvpPresenter.requestShiftInfo(getRequestParams(mvpPresenter.getStartIndex()));
                });
        dialog1.btn_neg.setVisibility(View.GONE);
        if (judgeBluetooth(dialog1)) {
            dialog1.btn_neg.setVisibility(View.GONE);
            dialog1.btn_pos.setVisibility(View.VISIBLE);
            dialog1.setPositiveButton("确定并打印", v -> {
                PrintUtils.printShitf(ShiftActivity.this, shiftBean);
                mvpPresenter.requestShiftInfo(getRequestParams(mvpPresenter.getStartIndex()));
                adapter.notifyDataSetChanged();
            });
        }
        dialog1.show();
    }

    private boolean judgeBluetooth(MyAlertDialog mDialog1) {
        if (MyApplication.getInstance().mService == null) {
            mDialog1.btn_pos.setVisibility(View.GONE);
            return false;
        }
        if (MyApplication.getInstance().mService.getState() != BluetoothService.STATE_CONNECTED) {
            mDialog1.btn_pos.setVisibility(View.GONE);
            return false;
        }
        return true;
    }

    @OnClick({R.id.iv_shift_back, R.id.tv_shift_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shift_back:
                finish();
                break;
            case R.id.tv_shift_submit:
                productShift();
                break;
        }
    }

    class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    isAnd = false;
                    mvpPresenter.requestShiftInfo(getRequestParams(mvpPresenter.getStartIndex()));
                    adapter.notifyDataSetChanged();
                    // 千万别忘了告诉控件刷新完毕了哦！
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 加载操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    isAnd = true;
                    noUpdate = false;
                    mvpPresenter.requestShiftInfo(getRequestParams(++page));
                    adapter.notifyDataSetChanged();
                    // 千万别忘了告诉控件加载完毕了哦！
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 1000);
        }
    }

    @Override
    public void hideLoading() {
        dismissLoading();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }
}
