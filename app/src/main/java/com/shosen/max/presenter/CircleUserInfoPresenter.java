package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CircleUserInfoPresenter extends
        RxPresenter<MessageContract.CircleUserInfoView>
        implements MessageContract.CircleUserInfoPresenter {

    private Context mContext;

    private ApiService apiService;

    public CircleUserInfoPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    @Deprecated
    @Override
    public void getFollowerList(String userId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("userId", userId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getFollowList(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    @Deprecated
    @Override
    public void delFollower(String follower) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("follower", follower);
        //map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).delFollow(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    @Override
    public void getSelfMess(String userId, int pageNum) {
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("userId", userId);
        map.put("pageNum", String.valueOf(pageNum));
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).selfMess(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showSelfMess(accept, pageNum);
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);

    }

    /**
     * 个人关注
     *
     * @param userId
     */
    @Override
    public void getSelfFollow(String userId, int pageNum) {
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("userId", userId);
        map.put("pageNum", String.valueOf(pageNum));
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).selfFollow(requestBody).
                compose(RxUtils.ToMain()).
                compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showSelfFollowers(accept);
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);

    }


    /**
     * @param status 点赞为1 取消为0
     */
    @Override
    public void addPraise(String status, String messId, int position) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("messId", messId);
        map.put("markStatus", status);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).updateMessMark(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        //更新点赞状态
                        mView.updatePraiseStatus(status, position);
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    @Override
    public void invokeFocus(String follower) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("follower", follower);
        map.put("user", LoginUtils.getUser());

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).addFollow(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showFocusBack(true, follower);
                        Log.d("tag", accept.toString());
                        mView.showErrorMessage(accept.getMsg());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);

    }

    @Override
    public void cancelFocus(String follower) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("follower", follower);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).delFollow(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showErrorMessage(accept.getMsg());
                        mView.showFocusBack(false, follower);
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    /**
     * 举报或者拉黑
     *
     * @param targetId targetId 目标用户ID
     * @param type     type 0拉黑 1举报
     * @param reason   举报原因，拉黑不填
     */
    @Override
    public void defriendAndComplain(String targetId, String type, String reason) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("targetId", targetId);
        map.put("type", type);
        if ("1".equals(type)) {
            map.put("reason", type);
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).defriendAndComplain(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showErrorMessage(accept.getMsg());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    /**
     * 取消拉黑
     *
     * @param targetId
     */
    @Override
    public void cancelDefriend(String targetId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("targetId", targetId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).cancelDefriend(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showErrorMessage(accept.getMsg());
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    @Override
    public void isRelation(String bUserId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("bUserId", bUserId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).isRelation(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.handleRelation(accept);
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }
}
