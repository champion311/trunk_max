package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.CircleContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CircleUserMinePresenter extends RxPresenter<CircleContract.UserInfoMineView>
        implements CircleContract.UserInfoMinePresenter {

    private Context mContext;

    private ApiService apiService;

    public CircleUserMinePresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }


    @Override
    public void getCountByUserId() {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("user", LoginUtils.getUser());

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getCountByUserId(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        mView.showCountByUserId(accept);
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
