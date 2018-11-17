package com.shosen.max.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.User;
import com.shosen.max.bean.eventbusevent.CircleFragmentRefreshEvent;
import com.shosen.max.constant.RegexConstants;
import com.shosen.max.presenter.LoginPresenter;
import com.shosen.max.presenter.contract.LoginContract;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity implements LoginContract.View {


    @BindView(R.id.ed_phonenumber)
    EditText edPhonenumber;
    @BindView(R.id.ed_verifycode)
    EditText edVerifycode;
    @BindView(R.id.tv_get_verify)
    TextView tvGetVerify;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;

    @BindView(R.id.tv_wechat_login)
    TextView tvWechatLogin;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;

    private LoginPresenter mPresenter;

//    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
//            Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
//            Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS,
//            Manifest.permission.WRITE_APN_SETTINGS};

    private static String TAG = "LoginActivity";

    private Map<String, String> weChatResMap;


    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new LoginPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }


    @OnClick({R.id.tv_login, R.id.tv_get_verify,
            R.id.tv_user_protocol, R.id.iv_back, R.id.tv_wechat_login})
    public void OnClick(View view) {
        String phoneNumber = edPhonenumber.getText().toString().trim();
        String verifyCode = edVerifycode.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_login:
                if (!RegexUtils.isMobileSimple(phoneNumber)) {
                    ToastUtils.show(this, "请填写正确的手机号");
                } else if (!RegexUtils.isMatch(RegexConstants.REGEX_VERIFY_CODE, verifyCode)) {
                    ToastUtils.show(this, "请填写正确的验证码");
                } else if (!tvUserProtocol.isSelected()) {
                    ToastUtils.show(this, "请同意用户协议");
                } else {
                    mPresenter.loginValidate(phoneNumber, verifyCode);
                }
                break;
            case R.id.tv_user_protocol:
                //用户协议按钮
                if (!tvUserProtocol.isSelected()) {
                    tvUserProtocol.setSelected(true);
                } else {
                    tvUserProtocol.setSelected(false);
                }
                break;
            case R.id.tv_get_verify:
                //获取验证码
                if (RegexUtils.isMobileSimple(phoneNumber)) {
                    mPresenter.getVerifyCode(phoneNumber);
                    startCountMethod();
                } else {
                    ToastUtils.show(mContext, "请输入正确的手机号");
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_wechat_login:
                //微信登陆
                UMShareConfig shareConfig = new UMShareConfig();
                shareConfig.isNeedAuthOnGetUserInfo = true;
                UMShareAPI mShareAPI = UMShareAPI.get(this);
                mShareAPI.setShareConfig(shareConfig);
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Log.d(TAG, "start");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Log.d(TAG, map.toString());
                        weChatResMap = map;
                        if (map != null) {
                            String openid = map.get("openid");
                            mPresenter.invokeWeChatLogin(openid);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Log.d(TAG, "error");
                        ToastUtils.show(LoginActivity.this, throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Log.d(TAG, "cancel");
                    }
                });
                break;
            default:
                break;

        }
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("登录");
        tvUserProtocol.setSelected(true);

    }

    /**
     * 登陆成功
     *
     * @param response
     */
    @Override
    public void loginSuccess(LoginResponse response) {
        User mUser = new User();
        mUser.setUserData(response);
        LoginUtils.putUser(mUser);
        finish();
        EventBus.getDefault().post(new CircleFragmentRefreshEvent(true));

    }

    @Override
    public void showErrorMessage(String errorMessage) {
        ToastUtils.show(this, errorMessage);
    }

    /**
     * 收取验证码成功
     *
     * @param phoneNumber
     */
    @Override
    public void verifyCodeBack(String phoneNumber) {

        ToastUtils.show(this, phoneNumber);
    }


    /**
     * 开始验证码倒计时
     */
    public void startCountMethod() {
        tvGetVerify.setClickable(false);
        Disposable disposable = RxUtils.countdown(60).subscribe(integer -> {
                    if (integer > 0) {
                        tvGetVerify.setText("剩余" + integer + "秒再次获取");
                    } else {
                        integer = 0;
                        tvGetVerify.setText("获取验证码");
                        tvGetVerify.setClickable(true);
                    }
                }
                , throwable -> {
                });
        mPresenter.addSubscribe(disposable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 当前微信用户没有注册时
     */
    @Override
    public void weChatLoginFailed() {
        if (weChatResMap == null) {
            return;
        }
        String openid = weChatResMap.get("openid");
        String name = weChatResMap.get("name");
        String headimg = weChatResMap.get("iconurl");
        Bundle bundle = new Bundle();
        bundle.putString("openId", openid);
        bundle.putString("name", name);
        bundle.putString("picStr", headimg);
        ActivityUtils.startActivity(RelatedActivity.class, bundle);
    }
}


