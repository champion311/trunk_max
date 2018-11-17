package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.LoginResponse;

import java.util.Map;

public interface SettingContract {

    interface View extends BaseView {

        void getLatestVersionSuccess();

        void showErrorMessage(String message);


        //绑定成功
        void weChatRegisterSuccess();

        //解绑成功
        void unBindWeChatSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void getLatestVersion();

        void invokeWeChatRegister(Map<String, Object> map);

        void invokeWeChatUnLock();

        void weChatLoginRegister(String openId);
    }
}
