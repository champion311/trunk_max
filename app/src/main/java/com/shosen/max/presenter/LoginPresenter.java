package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.BaseView;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.User;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.LoginContract;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginPresenter extends RxPresenter<LoginContract.View> implements
        LoginContract.Presenter {


    private ApiService apiService;

    private Context mContext;

    public LoginPresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }

    //登陆
    @Override
    public void loginValidate(String phoneNumber, String verifyCode) {
        HashMap<String, String> map = new HashMap<>(8);
        map.put("phone", phoneNumber);
        map.put("smsCode", verifyCode);
        map.put("invitorUserPhone", "");
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).loginValidate(requestBody).compose(RxUtils.ToMain()).
                compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.loginSuccess(accept);
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable.getMessage());
                    }
                });
        addSubscribe(di);
    }

    /**
     * 通过微信绑定
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
                        mView.loginSuccess(accept);

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
     * 根据openId查询是否已经微信绑定过
     *
     * @param openId
     */
    @Override
    public void invokeWeChatLogin(String openId) {
        if (TextUtils.isEmpty(openId)) {
            return;
        }
        HashMap<String, String> map = new HashMap<>(8);
        //TODO FIX
        //map.put("id", openId);
        map.put("openId", openId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).weChatLogin(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        if (accept.getCode() == 100) {
                            //已经注册
                            if (mView != null) {
                                Gson gson = new Gson();
                                LinkedTreeMap treeMap = (LinkedTreeMap) accept.getData();
                                LoginResponse mUer = gson.fromJson(gson.toJson(treeMap), LoginResponse.class);
                                mView.loginSuccess(mUer);
                                //mView.weChatLoginSuccess();
                            }
                        } else {
                            //注册没有成功
                            if (mView != null) {
                                mView.weChatLoginFailed();
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        Log.d("tag", throwable.toString());
                    }
                });

        addSubscribe(di);
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     */
    @Override
    public void getVerifyCode(String phoneNumber) {
        HashMap<String, String> map = new HashMap<>(8);
        map.put("phone", phoneNumber);
        map.put("invitorUserPhone", "");
        //map.put("code", "13520639126");
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).loginCode(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult())
                .subscribe(accept -> {
                    if (mView != null) {
                        mView.verifyCodeBack(accept.getMessage());
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.verifyCodeBack(throwable.getMessage());
                    }
                });

        addSubscribe(di);
    }

}
