package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.OrderCarContract;
import com.shosen.max.presenter.contract.PayContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 预定车
 */
public class OrderCarPresenter extends
        RxPresenter<OrderCarContract.View> implements OrderCarContract.Presenter {

    private ApiService apiService;

    private Context mContext;

    public OrderCarPresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }

    @Override
    public void bookOrder(Map<String, Object> map) {
        // Map<String, Object> map = new HashMap<String, Object>();
//        map.put("bookPhone", "18811447873");
//        map.put("bookStatus", "1");
//        map.put("bookMoney", "25000");
//        map.put("bookProvince", "北京");
//        map.put("bookCity", "北京");
        if (!LoginUtils.isLogin) {
            return;
        }
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).book(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
            if (mView != null) {
                mView.payBack(accept);
            }

        }, throwable -> {
            if (mView != null) {
                mView.showErrorMessage(throwable.toString());
            }
        });
        addSubscribe(di);
    }
}
