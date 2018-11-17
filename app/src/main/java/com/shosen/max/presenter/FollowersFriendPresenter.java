package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.CircleContract;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FollowersFriendPresenter extends RxPresenter<MessageContract.FollowerFriendsView>
        implements MessageContract.FollowerFriendsPresenter {

    private Context mContext;

    private ApiService apiService;

    public FollowersFriendPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

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
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showSelfFollowers(accept, pageNum);
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
    public void invokeFocus(String follower, int position) {
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
                        mView.showFocusBack(true, position);
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
    public void cancelFocus(String follower, int position) {
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
                        mView.showMessage(accept.getMsg());
                        mView.showFocusBack(false, position);
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
     * 获取我的粉丝
     *
     * @param pageNum
     */
    @Override
    public void getMyFans(int pageNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("pageNum", String.valueOf(pageNum));
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getMyFans(requestBody).
                compose(RxUtils.handleResult()).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showSelfFollowers(accept, pageNum);
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
     * 获取好友邀请
     *
     * @param pageNum
     */
    @Override
    public void getFriendInvitation(int pageNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("pageNum", String.valueOf(pageNum));
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getFriendInvitation(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.showSelfFollowers(accept, pageNum);
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
