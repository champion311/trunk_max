package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.bean.User;

import java.util.List;

public interface MineFragmentContract {

    interface View extends BaseView {

        void updateUserData(User updateUser);

        void updateDataFailed(String message);

        //没有登陆的时候获取剩余合伙人数目
        void getRemainNumSuccess(String remainNum);

        void showErrorMessage(String message);

        //字典数据
        void showDictionaryData(String type, List<DictionaryBean> mData);


    }

    interface Presenter extends BasePresenter<View> {
        void selectUserByPhone(String phoneNum);

        void getRemainOwner();

        //1是爱好，2是身份
        void getDictionaryData(String type);
    }


}
