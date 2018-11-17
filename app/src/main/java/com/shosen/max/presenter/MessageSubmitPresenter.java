package com.shosen.max.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.shosen.max.api.ApiService;
import com.shosen.max.base.RxPresenter;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.FileUploadResponse;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.network.RetrofitClient;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.utils.FileUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RxUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.utils.ToastUtils;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableAll;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import top.zibin.luban.Luban;

public class MessageSubmitPresenter extends
        RxPresenter<MessageContract.SubmitView> implements MessageContract.SubmitPresenter {

    private ApiService apiService;

    private Context mContext;

    public MessageSubmitPresenter(Context mContext) {
        this.mContext = mContext;
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    /**
     * 上传数据
     *
     * @param content
     * @param location 定位
     * @param picture  图片集合以,分割
     */
    @Override
    public void submitMessage(String content, String location, String picture) {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("content", content);
        map.put("location", location);
        map.put("picture", picture);
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).addMess(requestBody).
                compose(RxUtils.ToMain()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.submitSuccess(accept);
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
     * 上传单个图片
     */
    @Override
    public void uploadImage(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Disposable di = RetrofitClient.getInstance().
                create(ApiService.class).baseUpLoad(filePart).
                compose(RxUtils.ToMain()).compose(RxUtils.handleResult()).
                subscribe(accept -> {
                    if (mView != null) {
                        Log.d("tag", accept.toString());
                        mView.upLoadFileSuccess(accept);
                        //mView.showErrorMessage("file upload success");
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
     * 传递多张图片然后发布状态
     *
     * @param file
     */
    @Deprecated
    @Override
    public void uploadImagesAndSubmit(String content, String location, File... file) {
        List<Observable<FileUploadResponse>> observables = getRequests(file);
        Disposable di = Observable.zip(observables, objects -> {

            StringBuilder sb = new StringBuilder();
            for (Object object : objects) {
                if (object instanceof FileUploadResponse) {
                    if (sb.length() != 0) {
                        sb.append(",");
                    }
                    sb.append(((FileUploadResponse) object).getUrl());
                }
            }
            return sb.toString();
        }).flatMap(s -> getSubmitObservable(content, location, s)).
                compose(RxUtils.ToMain()).subscribe(accept -> {
            if (mView != null) {
                Log.d("tag", accept.toString());
                mView.submitSuccess(accept);
            }

        }, throwable -> {
            if (mView != null) {
                mView.showErrorMessage(throwable.toString());
            }
        });
        addSubscribe(di);
    }

    /**
     * 推荐使用
     *
     * @param content
     * @param location
     * @param files
     */
    public void uploadImagesAndSubmitOneTime(String content, String location, File... files) {
        Disposable di = getCompressedImage(mContext, files).flatMap(compressedfiles -> {
            for (File file : compressedfiles) {
                Log.d("MessageSubmitPresenter", file.getName() + ":after" + FileUtils.getFileFormatSize(file));
            }
            return getRequestsOneTime(compressedfiles.toArray(new File[compressedfiles.size()]));
        })
                .flatMap(s -> getSubmitObservable(content, location, s)).compose(RxUtils.ToMain()).
                        subscribe(accept -> {
                            if (mView != null) {
                                Log.d("tag", accept.toString());
                                mView.submitSuccess(accept);
                            }
                        }, throwable -> {
                            if (mView != null) {
                                mView.showErrorMessage(throwable.toString());
                            }
                        });
        addSubscribe(di);


//        Disposable di = getRequestsOneTime(files).
//                flatMap(s -> getSubmitObservable(content, location, s)).compose(RxUtils.ToMain()).
//                subscribe(accept -> {
//                    if (mView != null) {
//                        Log.d("tag", accept.toString());
//                        mView.submitSuccess(accept);
//                    }
//                }, throwable -> {
//                    if (mView != null) {
//                        mView.showErrorMessage(throwable.toString());
//                    }
//                });
        addSubscribe(di);

    }

    /**
     * 压缩图片的过程
     *
     * @param mContext
     * @param files
     * @return
     */
    public Observable<List<File>> getCompressedImage(Context mContext, File... files) {
        String path = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator;
        Observable<List<File>> ret = Observable.just(Arrays.asList(files)).observeOn(Schedulers.io())
                .map(new Function<List<File>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<File> list) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        for (File file : list) {
                            Log.d("MessageSubmitPresenter", file.getName() + ":before" + FileUtils.getFileFormatSize(file));
                        }
                        return Luban.with(mContext).load(list).setTargetDir(path).get();
                    }
                });
        return ret;

    }

    /**
     * 传递多张图片
     *
     * @param files
     * @return
     */
    public Observable<String> getRequestsOneTime(File... files) {

        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];
        for (int i = 0; i < files.length; i++) {
            RequestBody requestFileBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), files[i]);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", files[i].getName(), requestFileBody);
            parts[i] = filePart;
        }
        return RetrofitClient.getInstance().
                create(ApiService.class).baseUpLoadFiles(parts).
                compose(RxUtils.handleResult()).map(uploadResponse -> {
            return uploadResponse.getUrl();
        });
    }


    /**
     * 合并最多9次的请求
     *
     * @param files
     * @return
     */
    @Deprecated
    public List<Observable<FileUploadResponse>> getRequests(File... files) {
        List<Observable<FileUploadResponse>> observables =
                new ArrayList<>();
        for (File file : files) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Observable<FileUploadResponse> observable = RetrofitClient.getInstance().
                    create(ApiService.class).baseUpLoad(filePart).
                    compose(RxUtils.handleResult());
            observables.add(observable);
        }
        return observables;
    }


    //recommend
    public Observable<BaseResponse> getSubmitObservable(String content, String location, String picture) {
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("content", content);
        if (!TextUtils.isEmpty(location)) {
            map.put("location", location);
        }
        if (!TextUtils.isEmpty(picture)) {
            map.put("picture", picture);
        }
        map.put("userId", LoginUtils.getUser().getUid());
        map.put("user", LoginUtils.getUser());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        return RetrofitClient.getInstance().
                create(ApiService.class).addMess(requestBody);
    }
}
