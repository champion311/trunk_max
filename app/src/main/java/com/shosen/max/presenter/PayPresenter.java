package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.PayRequest;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.PayContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PayPresenter extends RxPresenter<PayContract.View> implements PayContract.Presenter {


    private Context mContext;

    private ApiService apiService;

    public PayPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    /**
     * 调用微信支付
     *
     * @param bookId
     */
    @Override
    public void invokeWxPay(String bookId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", LoginUtils.getUser());
        map.put("bookId", bookId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).
                invokeWxPay(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {

            Log.d("PayPresenter", accept.toString());
            if (mView != null) {
                mView.invokeWxPayReqBack(accept);
            }
        }, throwable -> {
            Log.d("PayPresenter", throwable.toString());
            if (mView != null) {
                mView.showErrorMessage(throwable.toString());
            }
        });
        addSubscribe(di);

    }

    /**
     * 调用支付宝支付
     *
     * @param bookId
     */
    @Override
    public void invokeAliPay(String bookId) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", LoginUtils.getUser());
        map.put("bookId", bookId);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).
                invokeAliPay(requestBody).compose(RxUtils.ToMain())
                .compose(RxUtils.handleResult()).subscribe(accept -> {
                    Log.d("PayPresenter", accept.toString());
                    if (mView != null) {
                        mView.invokeAliPayReqBack(accept);
                    }
                }, throwable -> {
                    Log.d("PayPresenter", throwable.toString());
                    if (mView != null) {
                        mView.showErrorMessage(throwable.toString());
                    }
                });
        addSubscribe(di);
    }
}
