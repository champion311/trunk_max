package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.User;

import java.util.HashMap;
import java.util.Map;

public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(LoginResponse response);

        void showErrorMessage(String errorMessage);

        void verifyCodeBack(String phoneNumber);


        void weChatLoginFailed();
    }

    interface Presenter extends BasePresenter<View> {

        void loginValidate(String phoneNumber, String verifyCode);

        void getVerifyCode(String phoneNumber);

        void invokeWeChatRegister(Map<String, Object> map);

        void invokeWeChatLogin(String openId);
    }
}
