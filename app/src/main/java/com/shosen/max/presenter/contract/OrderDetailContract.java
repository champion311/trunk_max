package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.OrderResponse;

public interface OrderDetailContract {

    interface View extends BaseView {

        void showOrderDetails(OrderResponse response);

        void cancelOrderSuccess();

        void delOrderSuccess();

        void showErrorMessage(String message);

    }

    interface Presenter extends BasePresenter<View> {

        void getOrderDetail(String bookId);

        void cancelOrder(String bookId, String cancelReason);

        void delOrder(String bookId, String delReason);

        void payContinue();

    }
}
