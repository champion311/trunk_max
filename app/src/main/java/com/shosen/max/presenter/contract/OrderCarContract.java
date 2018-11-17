package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.OrderCarResponse;
import com.shosen.max.bean.OrderResponse;

import java.util.Map;

public interface OrderCarContract {

    interface View extends BaseView {

        void payBack(OrderResponse response);

        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void bookOrder(Map<String, Object> map);
    }
}
