package com.shosen.max.presenter.contract;

import android.widget.ListView;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.QuestionnaireBean;

import java.util.List;
import java.util.Map;

public interface QuestionnaireContract {

    interface View extends BaseView {
        void showQuestionData(List<QuestionnaireBean> mData);

        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter<View>, BaseView {
        void getData();
    }
}
