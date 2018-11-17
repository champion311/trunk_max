package com.shosen.max.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.SpanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author TMAC
 */
public class MessagePresenter extends
        RxPresenter<MessageContract.View>
        implements MessageContract.Presenter {

    private ApiService apiService;

    private Context mContext;

    public static final int ADD_COMMENT_SUCCESS = 100;

    public static final int DEL_COMMENT_SUCCESS = 101;


    public MessagePresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }


    /**
     * 获取评论列表
     *
     * @param messId
     */
    @Override
    public void getComments(String messId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("user", LoginUtils.getUser());
        map.put("messId", messId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getComments(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showCommentsList(accept);
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

    /**
     * @param content  内容
     * @param messId   朋友圈id
     * @param parentId 回复的评论ID 没有默认0 目标targetId
     * @param userId   评论用户ID
     */
    @Override
    public void addComments(String content, String messId, String parentId, String userId) {
        HashMap<String, Object> map = new HashMap<>(8);
        String submitContent = SpanUtils.getUrlEncodeContent(content);
        map.put("user", LoginUtils.getUser());
        map.put("content", submitContent);
        map.put("messId", messId);
        map.put("parentId", parentId);
        map.put("userId", userId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).addComment(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.acitionCallBack(ADD_COMMENT_SUCCESS);
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

    /**
     * 删除评论回复
     *
     * @param id
     */
    @Override
    public void delComments(String id) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("id", id);
        map.put("user", LoginUtils.getUser());
        map.put("userId", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).delComment(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.acitionCallBack(DEL_COMMENT_SUCCESS);
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

    /**
     * 消息点赞
     *
     * @param status 点赞为1 取消为0
     * @param messId messId 评论id
     */
    @Override
    public void addMessPraise(String status, String messId) {
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
                        mView.updatePraiseStatus(status, -1);
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
     * 评论点赞
     *
     * @param status    点赞为1 取消为0
     * @param commentId
     */
    @Override
    public void addCommentPraise(String status, String commentId, int position) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("commentId", commentId);
        map.put("markStatus", status);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).updateCommentMark(requestBody).
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


}
