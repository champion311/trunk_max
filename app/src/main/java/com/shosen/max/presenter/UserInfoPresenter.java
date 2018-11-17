package com.shosen.max.presenter;

import android.content.Context;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.R;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.constant.TimeConstants;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.PayContract;
import com.shosen.max.presenter.contract.UserContract;
import com.shosen.max.utils.CacheUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoPresenter extends
        RxPresenter<UserContract.View> implements UserContract.Presenter {


    private Context mContext;

    private ApiService apiService;

    public UserInfoPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    @Deprecated
    @Override
    public void updateUserInfo(File file, String userName, String tabs) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, RequestBody> partMap = new HashMap<>();
        RequestBody idBody = RequestBody.create(MediaType.parse("multipart/form-data"), LoginUtils.getUser().getUid());
        RequestBody tabsBody = RequestBody.create(MediaType.parse("multipart/form-data"), tabs);
        RequestBody nameBody = RequestBody.create(MediaType.parse("multipart/form-data"), userName);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), LoginUtils.getUser().getSecurityToken());
        partMap.put("id", idBody);
        partMap.put("tabs", tabsBody);
        partMap.put("name", nameBody);
        partMap.put("user.securityToken", tokenBody);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Disposable di = RetrofitClient.getInstance().create(ApiService.class).updateUserInfo(partMap, filePart).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
            Log.d("UserInfoPresenter", accept.toString());
            mView.updateSuccess(accept);
        }, throwable -> {
            Log.d("UserInfoPresenter", throwable.toString());
            if (mView != null) {
                mView.showErrorMessage(throwable.getMessage());
            }
        });
        addSubscribe(di);

    }


    /**
     * 将非文件map部分转化为multipart形式
     *
     * @param map
     * @return
     */
    public Map<String, RequestBody> convertMultiPartHash(HashMap<String, String> map) {
        if (map == null) {
            return null;
        }
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                break;
            }
            RequestBody rb =
                    RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
            partMap.put(entry.getKey(), rb);
        }
        return partMap;
    }

    @Override
    public void updateUserInfo(File file, HashMap<String, String> maps) {
        if (!LoginUtils.isLogin) {
            return;
        }
        Map<String, RequestBody> partMap = convertMultiPartHash(maps);
        if (partMap == null) {
            return;
        }
        if (file != null) {
            MultipartBody.Part filePart = null;
            try {
                RequestBody requestFile;
                requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            } catch (Exception e) {
                e.printStackTrace();
                mView.showErrorMessage(e.getMessage());
            }
            Disposable di = RetrofitClient.getInstance().create(ApiService.class).updateUserInfo(partMap, filePart).
                    compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
                Log.d("UserInfoPresenter", accept.toString());
                mView.updateSuccess(accept);
            }, throwable -> {
                if (mView != null) {
                    mView.showErrorMessage(throwable.getMessage());
                }
            });
            addSubscribe(di);
        } else {
            //不传头像
            Disposable di = RetrofitClient.getInstance().create(ApiService.class).updateUserInfo(partMap).
                    compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(accept -> {
                Log.d("UserInfoPresenter", accept.toString());
                mView.updateSuccess(accept);
            }, throwable -> {
                Log.d("UserInfoPresenter", throwable.toString());
                if (mView != null) {
                    mView.showErrorMessage(throwable.getMessage());
                }
            });
            addSubscribe(di);
        }
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
            mView.showDictionaryData(type, accept);
            return;
        }
        Disposable di = RetrofitClient.getInstance().create(ApiService.class).getDictionary(type).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).subscribe(
                accept -> {
                    if (mView != null) {
                        mView.showDictionaryData(type, accept);
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
