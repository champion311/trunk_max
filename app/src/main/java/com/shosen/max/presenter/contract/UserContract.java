package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.UserDetetail;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface UserContract {

    interface View extends BaseView {
        void updateSuccess(UserDetetail mUserDetail);

        void showDictionaryData(String type, List<DictionaryBean> mData);

        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void updateUserInfo(File file, String userName, String tabs);

        void updateUserInfo(File file, HashMap<String, String> maps);

        //1是爱好，2是身份
        void getDictionaryData(String type);
    }
}
