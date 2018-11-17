package com.shosen.max.presenter;

import android.content.Context;
import android.util.Log;

import com.bin.david.form.data.form.IForm;
import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.bean.User;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MyOrderContract;
import com.shosen.max.presenter.contract.OrderDetailContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class OrderDetailPresenter extends RxPresenter<OrderDetailContract.View> implements
        OrderDetailContract.Presenter {

    private ApiService apiService;

    private Context mContext;

    public OrderDetailPresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }

    @Override
    public void getOrderDetail(String bookId) {
        if (!LoginUtils.isLogin) {
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookId", bookId);
//        User user = new User();
//        user.setSecurityToken("f278e038c5a74c4b896a6a06ea0d6a5b");
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                userBookDetail(requestBody).compose(RxUtils.ToMain()).subscribe(new Consumer<BaseResponse<OrderResponse>>() {
            @Override
            public void accept(BaseResponse<OrderResponse> response) throws Exception {
                mView.showOrderDetails(response.getData());
                Log.d("tag", response.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showErrorMessage(throwable.toString());
                Log.d("tag", throwable.toString());
            }
        });
        addSubscribe(di);
    }

    @Override
    public void cancelOrder(String bookId, String cancelReason) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookId", bookId);
        map.put("cancelReason", cancelReason);
//        User user = new User();
//        user.setSecurityToken("8c953b0f85b1466b892583a2eb019a78");
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                cancelBook(requestBody).compose(RxUtils.ToMain()).subscribe(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                // mView.showOrderDetails(response.getData());
                Log.d("tag", response.toString());
                mView.cancelOrderSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showErrorMessage(throwable.toString());
                Log.d("tag", throwable.toString());
            }
        });
        addSubscribe(di);

    }

    @Override
    public void delOrder(String bookId, String delReason) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookId", bookId);
        map.put("cancelReason", delReason);
//        User user = new User();
//        user.setSecurityToken("8c953b0f85b1466b892583a2eb019a78");
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));

        Disposable di = RetrofitClient.getInstance().create(ApiService.class).
                delBook(requestBody).compose(RxUtils.ToMain()).subscribe(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                // mView.showOrderDetails(response.getData());
                Log.d("tag", response.toString());
                mView.delOrderSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showErrorMessage(throwable.toString());
                Log.d("tag", throwable.toString());
            }
        });
        addSubscribe(di);

    }

    @Override
    public void payContinue() {

    }
}
