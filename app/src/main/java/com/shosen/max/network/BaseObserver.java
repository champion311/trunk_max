package com.shosen.max.network;


import android.util.Log;

import com.shosen.max.bean.BaseResponse;
import com.shosen.max.constant.Contstants;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    private static final String TAG = "BaseObserver";

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        //TODO 处理错误代码
        if (tBaseResponse.getCode() == Contstants.NET_SUCCESS) {
            handleSuccess(tBaseResponse.getData());
        } else {
            //处理code错误
            handleCodeError(tBaseResponse.getCode());
        }
    }

    @Override
    public void onError(Throwable t) {
        //TODO 处理异常
        Log.d(TAG, t.toString());
        if (t instanceof SocketTimeoutException) {
            Log.d(TAG, "socketTimeOutException");
        }

    }

    @Override
    public void onComplete() {

    }

    public abstract void handleSuccess(T t);

    public abstract void handleCodeError(int code);
}
