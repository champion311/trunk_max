package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.AlipayReqBean;
import com.shosen.max.bean.WxPayBean;

import java.util.Map;

public interface PayContract {

    interface View extends BaseView {

        void invokeAliPayReqBack(AlipayReqBean aliBean);

        void invokeWxPayReqBack(WxPayBean wxBean);

        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void invokeWxPay(String bookId);

        void invokeAliPay(String bookId);
    }
}
