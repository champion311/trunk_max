package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MyAllowanceContract;
import com.shosen.max.presenter.contract.MyAllowanceListContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyAllowanceListPresenter extends
        RxPresenter<MyAllowanceListContract.View> implements MyAllowanceListContract.Presenter {

    private ApiService apiService;

    private Context mContext;

    public MyAllowanceListPresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }

    @Override
    public void getListData() {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", LoginUtils.getUser());
        map.put("phone", LoginUtils.getUser().getPhone());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).rewardList(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
            if (mView != null) {
                Log.d("tag", accept.toString());
                mView.dataSuccess(accept);
            }

        }, throwable -> {
            if (mView != null) {
                Log.d("tag", throwable.toString());
                mView.dataError(throwable.getMessage());
            }//
        });
        addSubscribe(di);
    }
}
