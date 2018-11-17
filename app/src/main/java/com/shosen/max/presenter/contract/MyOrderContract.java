package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.OrderResponse;

import java.util.List;

public interface MyOrderContract {

    interface View extends BaseView {

        void showBookList(List<OrderResponse> mList);

        void showError(String errorMessage);

    }

    interface Presenter extends BasePresenter<View> {

        void getOrdersList(String bookPhone);


    }
}
