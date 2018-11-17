package com.shosen.max.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;


public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;

    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }


    @Override
    public void subscribe(T view) {
        this.mView = view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
        unSubscribe();
    }
}
