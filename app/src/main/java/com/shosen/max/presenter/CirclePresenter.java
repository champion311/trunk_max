package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.User;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.CircleContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CirclePresenter extends
        RxPresenter<CircleContract.View> implements CircleContract.Presenter {

    private Context mContext;

    private ApiService apiService;

    public CirclePresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    @Override
    public void getCircleData(int pageNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        //TODO 待修改
        map.put("user", LoginUtils.getUser());
        map.put("pageNum", String.valueOf(pageNum));
        map.put("userId", LoginUtils.getUser().getUid());
        //map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).selectFriendCircle(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showCircleData(accept, pageNum);
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

//    @Deprecated
//    @Override
//    public void refreshCircleData() {
//        if (!LoginUtils.isLogin) {
//            return;
//        }
//        HashMap<String, Object> map = new HashMap<>(8);
////        map.put("userId", LoginUtils.getUser().getUid());
//        map.put("user", LoginUtils.getUser());
//        map.put("userId", "12");
//        map.put("pageNum", "1");
//        RequestBody requestBody = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
//        Disposable di = RetrofitClient.getInstance().
//                create(ApiService.class).refreshMess(requestBody).
//                compose(RxUtils.ToMain()).
//                subscribe(accept -> {
//                    if (mView != null) {
//                        Log.d("tag", accept.toString());
//                    }
//                }, throwable -> {
//                    if (mView != null) {
//                        Log.d("tag", throwable.toString());
//                        mView.showErrorMessage(throwable.getMessage());
//                    }
//                });
//        addSubscribe(di);
//    }

    /**
     * @param status   点赞为1 取消为0
     * @param messId
     * @param position
     */
    @Override
    public void addPraise(String status, String messId, final int position) {
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

    /**
     * 删除message
     *
     * @param id
     */
    @Override
    public void delMessage(String id, int position) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("id", id);
        map.put("messId", id);
        map.put("user", LoginUtils.getUser());
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).delMess(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.delMessageCallBack(position);
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
}
