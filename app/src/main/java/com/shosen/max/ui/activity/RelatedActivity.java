package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.User;
import com.shosen.max.presenter.LoginPresenter;
import com.shosen.max.presenter.contract.LoginContract;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * 关联使用activity
 */
public class RelatedActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.ed_phonenumber)
    EditText edPhonenumber;
    @BindView(R.id.ed_verifycode)
    EditText edVerifycode;
    @BindView(R.id.tv_get_verify)
    TextView tvGetVerify;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_wechat_login)
    TextView tvWechatLogin;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    private LoginPresenter mPresenter;

    private String openId;

    private String name;

    private String picStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new LoginPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        if (getIntent() != null) {
            openId = getIntent().getStringExtra("openId");
            name = getIntent().getStringExtra("name");
            picStr = getIntent().getStringExtra("picStr");
        }
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        ivLogo.setVisibility(View.GONE);
        tvHeadTitle.setText("关联手机号");
        tvUserProtocol.setVisibility(View.GONE);
        tvWechatLogin.setVisibility(View.GONE);
        tvLogin.setText("完成");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.iv_back, R.id.tv_get_verify, R.id.tv_login})
    public void OnClick(View view) {
        String phoneNumber = edPhonenumber.getText().toString().trim();
        String verifyCode = edVerifycode.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
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
            case R.id.tv_login:
                if (!RegexUtils.isMobileSimple(phoneNumber)) {
                    ToastUtils.show(this, "请填写正确的手机号");
                } else if (!RegexUtils.isMatch("^\\d{4}$", verifyCode)) {
                    ToastUtils.show(this, "请填写正确的验证码");
                } else if (RegexUtils.isMobileSimple(phoneNumber)) {
                    HashMap<String, Object> reqMap = new HashMap<>(16);
                    reqMap.put("openId", openId);
                    reqMap.put("name", name);
                    reqMap.put("picStr", picStr);
                    //TODO FIX
                    reqMap.put("phone", phoneNumber);
                    reqMap.put("smsCode", verifyCode);
                    mPresenter.invokeWeChatRegister(reqMap);
                }
                break;
        }
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
        ActivityUtils.finishActivity(LoginActivity.class);
        finish();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        ToastUtils.show(this, errorMessage);
    }

    @Override
    public void verifyCodeBack(String phoneNumber) {
        ToastUtils.show(this, phoneNumber);
    }


    @Override
    public void weChatLoginFailed() {

    }
}
