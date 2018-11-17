package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.bean.GetCountByUserIdBean;

import java.util.List;

public interface CircleContract {

    interface View extends BaseView {

        void showErrorMessage(String message);

        void showCircleData(List<FriendCircleBean> mData, int pageNum);

        void updatePraiseStatus(String status, int position);

        void delMessageCallBack(int position);
    }

    interface Presenter extends BasePresenter<View> {

        void getCircleData(int pageNum);

        /**
         * @param status 点赞为1 取消为0
         */
        void addPraise(String status, String messId, int position);

        void delMessage(String id, int position);
    }


    interface FindingView extends BaseView {
        void showBannerList(List<String> data);

        void showFriendInvitation(List<FriendInvitationBean> mData);

        void showErrorMessage(String message);

        void showCircleData(List<FriendCircleBean> mData, int pageNum);

        void updatePraiseStatus(String status, int position);

        void delMessageCallBack(int position);

    }


    interface FindingPresenter extends BasePresenter<FindingView> {

        void getBannerList(int type);

        void getFriendInvitation(int pageNum);

        void getHotTop(int pageNum);

        /**
         * @param status 点赞为1 取消为0
         */
        void addPraise(String status, String messId, int position);

        void delMessage(String id, int position);
    }

    interface UserInfoMineView extends BaseView {
        void showCountByUserId(GetCountByUserIdBean bean);

        void showErrorMessage(String message);

    }

    interface UserInfoMinePresenter extends BasePresenter<UserInfoMineView> {
        void getCountByUserId();
    }
}
