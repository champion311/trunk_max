package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.SettingContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SettingPresenter extends RxPresenter<SettingContract.View>
        implements SettingContract.Presenter {

    private Context mContext;

    private ApiService apiService;

    public SettingPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }


    @Deprecated
    @Override
    public void getLatestVersion() {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getLatestVerison(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
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


    /**
     * 微信解绑
     */
    @Override
    public void invokeWeChatUnLock() {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        map.put("id", LoginUtils.getUser().getUid());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).weChatUnLock(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.unBindWeChatSuccess();
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
     * 调用微信绑定(不需要手机号)
     *
     * @param openId
     */
    @Override
    public void weChatLoginRegister(String openId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("user", LoginUtils.getUser());
        map.put("openId", openId);
        map.put("phone", LoginUtils.getUser().getPhone());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).weChatLoginRegister(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.weChatRegisterSuccess();
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
     * 调用微信绑定
     *
     * @param map
     */
    @Override
    public void invokeWeChatRegister(Map<String, Object> map) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).weChatRegister(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.weChatRegisterSuccess();

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
