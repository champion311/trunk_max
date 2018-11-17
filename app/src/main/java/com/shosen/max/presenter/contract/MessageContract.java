package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.CommentBean;
import com.shosen.max.bean.FileUploadResponse;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.bean.MyRessMessBean;
import com.shosen.max.bean.RelationBean;

import java.io.File;
import java.util.List;

public interface MessageContract {

    interface View extends BaseView {

        void showErrorMessage(String message);

        void showCommentsList(List<CommentBean> mData);

        void acitionCallBack(int type);

        //更新点赞状态 postion=-1时为消息点赞
        void updatePraiseStatus(String status, int position);

    }


    interface Presenter extends BasePresenter<View> {

        void getComments(String messId);

        void addComments(String content, String messId, String parentId, String userId);

        void delComments(String id);

        /**
         * @param status 点赞为1 取消为0
         * @param messId messId 评论id
         */
        void addMessPraise(String status, String messId);

        void addCommentPraise(String status, String commentId, int position);

    }


    interface SubmitView extends BaseView {

        void showErrorMessage(String message);

        void upLoadFileSuccess(FileUploadResponse response);

        void submitSuccess(BaseResponse response);
    }

    interface SubmitPresenter extends BasePresenter<SubmitView> {

        /**
         * 提交请求
         *
         * @param content
         * @param location
         * @param picture
         */
        void submitMessage(String content, String location, String picture);

        /**
         * @param file 上传单张图片
         */
        void uploadImage(File file);

        /**
         * @param file 上传多张图片然后提交
         */
        void uploadImagesAndSubmit(String content, String location, File... file);
    }

    interface CircleUserInfoPresenter extends BasePresenter<CircleUserInfoView> {

        void getFollowerList(String uid);

        void delFollower(String follower);

        //个人消息列表
        void getSelfMess(String userId, int pageNum);

        //个人关注的好友
        void getSelfFollow(String userId, int pageNum);

        /**
         * @param status 点赞为1 取消为0
         */
        void addPraise(String status, String messId, int position);

        void invokeFocus(String follower);

        void cancelFocus(String follower);

        //参数： userId 用户ID
        //targetId 目标用户ID
        //type 0拉黑 1举报
        //reason  举报原因，拉黑不填

        /**
         * @param targetId targetId 目标用户ID
         * @param type     type 0拉黑 1举报
         * @param reason   举报原因，拉黑不填
         */
        void defriendAndComplain(String targetId, String type, String reason);

        /**
         * 取消拉黑
         *
         * @param targetId
         */
        void cancelDefriend(String targetId);

        void isRelation(String bUserId);

    }

    interface CircleUserInfoView extends BaseView {
        void showErrorMessage(String message);

        void showSelfFollowers(List<FriendInvitationBean> friendInvitationBeans);

        void showSelfMess(List<FriendCircleBean> friendCircleBeans, int pageNum);

        //更新点赞状态 postion=-1时为消息点赞
        void updatePraiseStatus(String status, int position);

        void showFocusBack(boolean isFocus, String follow);

        void handleRelation(RelationBean bean);

    }


    interface FollowerFriendsPresenter extends BasePresenter<FollowerFriendsView> {
        //个人关注的好友
        void getSelfFollow(String userId, int pageNum);

        void getFriendInvitation(int pageNum);

        void invokeFocus(String follower, int position);

        void cancelFocus(String follower, int position);

        void getMyFans(int pageNum);

    }

    interface FollowerFriendsView extends BaseView {
        void showSelfFollowers(List<FriendInvitationBean> mData, int pageNum);

        void showErrorMessage(String message);

        void showMessage(String message);

        void showFocusBack(boolean isFocus, int position);
    }

    interface MyMessagePresenter extends BasePresenter<MyMessageView> {

        void getMyReMesss(int pageNum);

        void getNoticeMess();

    }

    interface MyMessageView extends BaseView {
        void showErrorMessage(String message);

        void showMineMessageList(List<MyRessMessBean> mData, int pageNum);
    }


    interface MyHotTopPresenter extends BasePresenter<MyHotTopView> {
        void getMyHotTopic(String content, int pageNum);

        /**
         * @param status 点赞为1 取消为0
         */
        void addPraise(String status, String messId, int position);

        void delMessage(String id, int position);
    }

    interface MyHotTopView extends BaseView {


        void showErrorMessage(String message);

        void updatePraiseStatus(String status, int position);

        void delMessageCallBack(int position);

        void showSelfMess(String content, List<FriendCircleBean> mData, int pageNum);
    }

}
