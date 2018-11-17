package com.shosen.max.network;

import io.reactivex.disposables.Disposable;

public class BaseObserverImp<T> extends BaseObserver<T> {
    @Override
    public void handleSuccess(T t) {

    }

    @Override
    public void handleCodeError(int code) {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
