package com.shosen.max.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MyAllowanceContract;
import com.shosen.max.presenter.contract.UserContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyAllowancePresenter extends
        RxPresenter<MyAllowanceContract.View> implements MyAllowanceContract.Presenter {


    private ApiService apiService;

    private Context mContext;

    public MyAllowancePresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }

    @Override
    public void getMyAllowanceDetail() {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", LoginUtils.getUser());
        map.put("phone", LoginUtils.getUser().getPhone());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).rewardTotal(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
            if (mView != null) {
                mView.dataSuccess(accept);
            }

        }, throwable -> {
            if (mView != null) {
                mView.dataError(throwable.toString());
            }//
        });
        addSubscribe(di);
//
//        Disposable di2 = RetrofitClient.getInstance().
//                create(ApiService.class).rewardList(requestBody).
//                compose(RxUtils.ToMain()).subscribe(accept -> {
//            if (mView != null) {
//                mView.dataSuccess();
//            }
//
//        }, throwable -> {
//            if (mView != null) {
//                mView.dataSuccess();
//            }
//        });
//        addSubscribe(di2);

    }
}
