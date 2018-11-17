package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.User;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MyOrderContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyOrderListPresenter extends RxPresenter<MyOrderContract.View> implements
        MyOrderContract.Presenter {

    private ApiService apiService;

    private Context mContext;

    public MyOrderListPresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }


    @Override
    public void getOrdersList(String bookPhone) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookPhone", bookPhone);
//        User user = new User();
//        user.setSecurityToken("f278e038c5a74c4b896a6a06ea0d6a5b");
        User user = LoginUtils.mUser;
        map.put("user", user);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).userBookList(requestBody).compose(RxUtils.ToMain()).
                subscribe(new Consumer<BaseResponse<List<OrderResponse>>>() {
                              @Override
                              public void accept(BaseResponse<List<OrderResponse>> response) throws Exception {
                                  Log.d("tag", response.toString());
                                  //mView.loginSuccess(loginResponseBaseResponse.getData());
                                  mView.showBookList(response.getData());
                              }
                          }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("tag", throwable.toString());
                                mView.showError(throwable.toString());

                            }
                        });
        addSubscribe(di);
    }
}
