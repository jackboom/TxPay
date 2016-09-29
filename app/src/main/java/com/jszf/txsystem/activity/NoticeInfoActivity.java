package com.jszf.txsystem.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jszf.txsystem.R;
import com.jszf.txsystem.core.mvp.base.BaseActivity;

public class NoticeInfoActivity extends BaseActivity {
    private WebView mWebView;
    private String mContent;    //数据内容
    private TextView tv_web;
    private ImageView iv_message_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_info);
       setStatusBar();
        mWebView = (WebView) findViewById(R.id.web_notice);
        iv_message_back = (ImageView) findViewById(R.id.iv_message_back);
        iv_message_back.setOnClickListener(this);
        mContent = getIntent().getStringExtra("content");
        setData();
    }

    //加载富文本内容
    private void setData() {
        mWebView.loadDataWithBaseURL(null, mContent, "text/html", "UTF-8", null);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
