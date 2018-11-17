package com.shosen.max.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shosen.max.R;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    protected T mPresenter;

    protected Activity mActivity;

    public void setPresenter(T mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ButterKnife.bind(this, view);
        initEventAndData();
        if (mPresenter != null) {
            mPresenter.subscribe(this);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }

    protected abstract void initEventAndData();


}
