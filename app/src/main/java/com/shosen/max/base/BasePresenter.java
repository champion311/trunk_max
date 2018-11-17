package com.shosen.max.base;

public interface BasePresenter<T extends BaseView> {

    void subscribe(T view);//绑定

    void unsubscribe();//解绑
}
