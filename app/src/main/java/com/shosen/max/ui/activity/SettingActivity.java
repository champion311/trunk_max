package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.constant.TimeConstants;
import com.shosen.max.presenter.SettingPresenter;
import com.shosen.max.presenter.contract.SettingContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements
        SettingContract.View, UMAuthListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.fl_wechat_bind)
    FrameLayout flWechatBind;
    @BindView(R.id.fl_contact_us)
    FrameLayout flContactUs;
    @BindView(R.id.fl_app_version)
    FrameLayout flAppVersion;
    @BindView(R.id.fl_login_orquit)
    FrameLayout flLoginOrquit;
    @BindView(R.id.tv_login_orquit)
    TextView tvLoginOrquit;
    @BindView(R.id.tv_bind_text)
    TextView tvBindText;

    private SettingPresenter mPresenter;


    private static final String TAG = "SettingActivity";

    private Map<String, String> weChatResMap;

    private boolean isBinded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new SettingPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("设置");
        if (LoginUtils.isLogin) {
            isBinded = !TextUtils.isEmpty(LoginUtils.getUser().getOpenId());
            if (isBinded) {
                tvBindText.setText("解绑");
            } else {
                tvBindText.setText("去绑定");
            }
            tvLoginOrquit.setText("退出登录");
        } else {
            tvLoginOrquit.setText("注册/登录");
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_setting;
    }

    private long lastModifyMills = 0;

    @OnClick({R.id.iv_back, R.id.fl_login_orquit, R.id.fl_wechat_bind, R.id.fl_app_version})
    public void onClick(View view) {
        //防止重复点击
        if (System.currentTimeMillis() - lastModifyMills < TimeConstants.SEC) {
            lastModifyMills = System.currentTimeMillis();
            return;
        }
        lastModifyMills = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_login_orquit:
                if (LoginUtils.isLogin) {
                    LoginUtils.quitLogin();
                    //startActivity(new Intent(getActivity(), LoginActivity.class));
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.fl_wechat_bind:
                if (!LoginUtils.isLogin) {
                    return;
                }
                if (!isBinded) {
                    UMShareConfig shareConfig = new UMShareConfig();
                    shareConfig.isNeedAuthOnGetUserInfo = true;
                    UMShareAPI mShareAPI = UMShareAPI.get(this);
                    mShareAPI.setShareConfig(shareConfig);
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, SettingActivity.this);
                } else {
                    mPresenter.invokeWeChatUnLock();
                }
                break;
            case R.id.fl_app_version:
                break;
        }
    }

    @Override
    public void getLatestVersionSuccess() {

    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    @Override
    public void weChatRegisterSuccess() {
        ToastUtils.show(this, "绑定成功");
        tvBindText.setText("解绑");
        isBinded = true;
    }


    @Override
    public void unBindWeChatSuccess() {
        ToastUtils.show(this, "解绑成功");
        tvBindText.setText("去绑定");
        isBinded = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //umeng回调
    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        Log.d(TAG, map.toString());
        weChatResMap = map;
        if (map != null) {
            String openid = map.get("openid");
            mPresenter.weChatLoginRegister(openid);
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        ToastUtils.show(this, throwable.getMessage());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {

    }
}
