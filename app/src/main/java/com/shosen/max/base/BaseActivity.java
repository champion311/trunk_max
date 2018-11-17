package com.shosen.max.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shosen.max.R;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected static String tag = "";

    protected Context mContext;

    public static BaseActivity instance;


    protected T mPresenter;

    public void setPresenter(T mPresenter) {
        this.mPresenter = mPresenter;
    }

    public static BaseActivity getThis() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        tag = this.getClass().getSimpleName();
        mContext = this;
        if (getContentViewID() != 0) {
            setContentView(getContentViewID());
            ButterKnife.bind(this);
        }
        //进行事件绑定
        if (mPresenter != null) {
            mPresenter.subscribe(this);
        }
        super.onCreate(savedInstanceState);
        initViewAndEvents();
        initTopHeader();
    }

    public void initTopHeader() {
        RelativeLayout rlTop = (RelativeLayout) findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(this);
            rlTop.setLayoutParams(layoutParams);
        }
    }


    @Override
    protected void onDestroy() {
        //解除事件的绑定
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * initViews
     */
    protected abstract void initViewAndEvents();

    /**
     * bind activity_blogger resource file
     */
    protected abstract int getContentViewID();

}
