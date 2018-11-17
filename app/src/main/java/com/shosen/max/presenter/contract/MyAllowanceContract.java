package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.RewardTotalResponse;
import com.shosen.max.bean.UserDetetail;

import java.io.File;

public interface MyAllowanceContract {

    interface View extends BaseView {
        void dataSuccess(RewardTotalResponse response);

        void dataError(String message);

    }

    interface Presenter extends BasePresenter<View> {
        void getMyAllowanceDetail();
    }
}
