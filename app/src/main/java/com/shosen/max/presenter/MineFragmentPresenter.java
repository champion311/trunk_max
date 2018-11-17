package com.shosen.max.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.constant.TimeConstants;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MineFragmentContract;
import com.shosen.max.utils.CacheUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MineFragmentPresenter extends RxPresenter<MineFragmentContract.View> implements
        MineFragmentContract.Presenter {


    private ApiService apiService;

    private Context mContext;

    public MineFragmentPresenter(Context mContext) {
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.mContext = mContext;
    }

    /**
     * 更新用户详情
     *
     * @param phoneNum
     */
    @Override
    public void selectUserByPhone(String phoneNum) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phoneNum);
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).selectUserByPhone(requestBody).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
            if (mView != null) {
                mView.updateUserData(accept);
                Log.d("tag", accept.toString());
            }

        }, throwable -> {
            if (mView != null) {
                mView.updateDataFailed(throwable.toString());
                Log.d("tag", throwable.toString());
            }
        });
        addSubscribe(di);
    }

    @Override
    public void getRemainOwner() {
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).getRemainNum().
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {

            if (mView != null && accept != null) {
                mView.getRemainNumSuccess(accept.getRemainNum());
            }
            Log.d("tag", accept.toString());

        }, throwable -> {
            if (mView != null) {
                mView.updateDataFailed(throwable.getMessage());
            }
            Log.d("tag", throwable.toString());

        });
        addSubscribe(di);
    }

    /**
     * 获取字典数据
     *
     * @param type 1为爱好（标签） 2头衔
     */
    @Override
    public void getDictionaryData(final String type) {
        Gson gson = new Gson();
        CacheUtils cacheUtils = CacheUtils.getInstance(Contstants.DICTIONARY_CACHE);
        if (!TextUtils.isEmpty(cacheUtils.getString(type))) {
            List<DictionaryBean> accept = gson.fromJson(cacheUtils.getString(type),
                    new TypeToken<List<DictionaryBean>>() {
                    }.getType());
            if (mView != null) {
                mView.showDictionaryData(type, accept);
            }
            return;
        }
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).getDictionary(type).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showDictionaryData(type, accept);
                        //缓存保存7天
                        cacheUtils.put(type, gson.toJson(accept), 7 * TimeConstants.DAY);
                    }

                }, throwable -> {
                    if (mView != null) {
                        mView.showErrorMessage(throwable.getMessage());
                    }
                }
        );
        addSubscribe(di);
    }
}
