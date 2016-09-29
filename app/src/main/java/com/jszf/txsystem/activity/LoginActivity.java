package com.jszf.txsystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.R;
import com.jszf.txsystem.bean.LoginBean;
import com.jszf.txsystem.bean.Merchant;
import com.jszf.txsystem.core.mvp.MvpActivity;
import com.jszf.txsystem.core.mvp.login.ILoginView;
import com.jszf.txsystem.core.mvp.login.LoginPresenterImpl;
import com.jszf.txsystem.util.Constant;
import com.jszf.txsystem.util.ParaUtils;
import com.jszf.txsystem.util.ProgressHUD;
import com.jszf.txsystem.util.RSAUtils;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpActivity<ILoginView,LoginPresenterImpl> implements TextView.OnEditorActionListener, ILoginView {
    @BindView(R.id.edt_username)
    EditText mEdtUsername;      //用户名
    @BindView(R.id.edt_password)
    EditText mEdtPassword;      //用户密码
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;     //忘记密码
    @BindView(R.id.iv_login_submit)
    ImageView mIvLoginSubmit;   //登录
    @BindView(R.id.iv_login_join)
    ImageView mIvLoginJoin;     //加入
    @BindView(R.id.iv_login_remember)
    ImageView mIvLoginRemember; //记住密码
    private SharedPreferences sp = null;// 声明一个SharedPreferences
    public static final String LOGININFOFILE = "loginInfo";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String REMBEMBERPASSWORD = "remeberPassword";
    private String userName;    //输入的账号
    private String userPassword;    //输入的密码
    private Handler mHandler = new Handler();
    private Merchant mMerchant;
    private ProgressHUD mProgressHUD;   //进度框对象
    private String dealCode;
    private String userLoginName;   //保存的账号
    private String userLoginPassword;   //保存的密码
    private Date mCurDate;      //当前时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        readAccount();
        mEdtPassword.setOnEditorActionListener(this);
    }

    @Override
    protected LoginPresenterImpl createPresenter() {
        return new LoginPresenterImpl(this);
    }

    private void readAccount() {
        sp = getSharedPreferences(LOGININFOFILE, MODE_PRIVATE);
        // 读取账号
        userLoginName = isUsername();
        if (!"".equals(userLoginName) && userLoginName != null) {
            mEdtUsername.setText(userLoginName);
        }
        // 判断是否选择记住密码
        if (isRemeberPassword()) {
            mIvLoginRemember.setImageResource(R.drawable.iconfont_kuang_change);
            mEdtPassword.setText(readPassword());
        }
        Log.d("LoginActivity", "userLoginName:" + userLoginName + ",userLoginPassword:" + userLoginPassword);
    }

    // 判断是否选择记住密码
    public boolean isRemeberPassword() {
        sp = getSharedPreferences(LOGININFOFILE, MODE_PRIVATE);
        String isRemPassword = sp.getString(REMBEMBERPASSWORD, "");
        if (TRUE.equals(isRemPassword)) {
            return true;
        }
        return false;
    }

    // 读取密码
    public String readPassword() {
        sp = getSharedPreferences(LOGININFOFILE, MODE_PRIVATE);
        String isRemPassword = sp.getString(PASSWORD, "");
        return isRemPassword;
    }

    @NonNull
    private HashMap<String, String> getRequestParams() {
        userLoginName = mEdtUsername.getText().toString().trim();
        userPassword = mEdtPassword.getText().toString().trim();
        final HashMap<String, String> maps = new HashMap<>();
        String mEncryptWord = null;
        try {
            mEncryptWord = RSAUtils.encrypt(userPassword, Constant.txPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        maps.put("service", "login");
        maps.put("userLoginName", userLoginName);
        maps.put("passWord", mEncryptWord);
        maps.put("version", "V1.0");
        maps.put("sign", RSAUtils.sign(ParaUtils.createLinkString(maps), Constant.ownPrivateKey));
        return maps;
    }

    /**
     * 判断数据是否为空或者网络
     *
     * @return
     */
    private boolean judgeData() {
        userName = mEdtUsername.getText().toString();
        userPassword = mEdtPassword.getText().toString().trim();
        Log.d("TAG", "userName:" + userName + ", userPassword:" + userPassword);
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)) {
            Toast.makeText(getApplicationContext(), "账户或密码为空，请确认输入！", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!judgeNetWord(LoginActivity.this)) {
            mHandler.post(() -> Toast.makeText(getApplicationContext(), "网络连接异常,请检查!", Toast.LENGTH_SHORT).show());
            return true;
        }
        return false;
    }

    /**
     * 记住密码
     */
    public void rememberPassword() {
        sp = getSharedPreferences(LOGININFOFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String name = mEdtUsername.getText().toString().trim();
        String passWord = mEdtPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(userName) || !userName.equals(name) ||
                !TextUtils.isEmpty(userPassword) || !userPassword.equals(passWord)) {
            editor.putString(USERNAME, name);
            editor.putBoolean("CHECKED", true);
            editor.putString(REMBEMBERPASSWORD, TRUE);
            editor.putString(PASSWORD, passWord);
            editor.commit();
            Log.d("LoginActivity", "记住密码---");
        }
    }

    /**
     * 判断是否存在账户
     *
     * @return 账户
     */
    public String isUsername() {
        sp = getSharedPreferences(LOGININFOFILE, MODE_PRIVATE);
        String isUsername = sp.getString(USERNAME, "");
        if (!"".equals(isUsername) && isUsername != null) {
            return isUsername;
        }
        return null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // 在这里编写自己想要实现的功能
            if (TextUtils.isEmpty(userName)) {
                userLoginName = mEdtUsername.getText().toString();
            } else {
                userLoginName = userName;
            }
            if (TextUtils.isEmpty(userPassword)) {
                userLoginPassword = mEdtPassword.getText().toString().trim();
            } else {
                userLoginPassword = userPassword;
            }
            mvpPresenter.requestLogin(getRequestParams());
            Log.d("TAG", "1");
            return true;
        }
        Log.d("TAG", "2");

        return false;
    }

    /**
     * 忘记密码
     */
    private void forgetPassword() {
        mTvForgetPassword.setTextColor(getResources().getColor(R.color.home_red));
        mHandler.postDelayed(() -> mTvForgetPassword.setTextColor(getResources().getColor(R.color.login_text)), 200);
        startActivity(new Intent(LoginActivity.this, FindAccountActivity.class));
    }

    // 清除记住密码标识
    public void disRemeberPassword() {
        sp = getSharedPreferences(LOGININFOFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERNAME, userName);
        editor.putBoolean("CHECKED", false);
        editor.putString(PASSWORD, userPassword);
        editor.putString(REMBEMBERPASSWORD, FALSE);
        editor.commit();
    }

    @OnClick({R.id.iv_login_remember, R.id.tv_forget_password, R.id.iv_login_submit, R.id.iv_login_join})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_remember:
                userName = mEdtUsername.getText().toString().trim();
                userPassword = mEdtPassword.getText().toString().trim();
                if (sp.getBoolean("CHECKED", false)) {
                    mIvLoginRemember.setImageResource(R.drawable.iconfont_kuang);
                    disRemeberPassword();
                } else {
                    mIvLoginRemember.setImageResource(R.drawable.iconfont_kuang_change);
                    rememberPassword();
                }
                break;
            case R.id.tv_forget_password:
                forgetPassword();
                break;
            case R.id.iv_login_submit:
                if (!judgeNetWord(LoginActivity.this)) {
                    Toast.makeText(getApplicationContext(), "网络连接异常,请检查!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mvpPresenter.requestLogin(getRequestParams());
                break;
            case R.id.iv_login_join:
                try {
                    Uri uri = Uri.parse(Constant.WEBSITE);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    it.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(it);
                    Log.d("LoginActivity", "跳转");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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

    @Override
    public void login(LoginBean loginBean) {
        try {
            String dealCode = loginBean.getDealCode();
            if (!dealCode.equals(Constant.DEAL_CODE_SUCCESS)) {
                mHandler.post(() -> Toast.makeText(getApplicationContext(), "账号或密码错误，请检查!", Toast.LENGTH_SHORT).show());
                return;
            }
            MyApplication.unEncryptMd5 = loginBean.getMD5key();
            MyApplication.merchantNo = loginBean.getMerchantNo();
            MyApplication.print_outlet = MyApplication.merchantNo;
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            MyApplication.userLoginName = userLoginName;
            startActivity(intent);
            Log.d("LoginActivity", MyApplication.unEncryptMd5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
