package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;

import java.util.List;

public interface HomePageContract {

    interface View extends BaseView {

        void showBannerList(List<String> data);

        void showErrorMessage(String message);

        void singSuccess(String message);

    }

    interface Presenter extends BasePresenter<View> {

        void getBannerList(int type);

        //签到
        void signIn();
    }

}
