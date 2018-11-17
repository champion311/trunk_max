package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.bean.RewardTotalResponse;

import java.util.List;

public interface MyAllowanceListContract {

    interface View extends BaseView {

        void dataSuccess(List<RewardListBean> rewardListBeanList);

        void dataError(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void getListData();

    }
}
