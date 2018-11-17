package com.shosen.max.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonDialog extends Dialog {

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;

    public CommonDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public CommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        //View view = View.inflate(getContext(), R.layout.dialog_cancel_order, null);
        window.setContentView(R.layout.dialog_common);
        ButterKnife.bind(this);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = ScreenUtils.getScreenWidth(getContext());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    public void setLeft(String text, @DrawableRes int drawableRes, View.OnClickListener onClickListener) {
        if (tvLeft == null) {
            return;
        }
        tvLeft.setText(text);
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
        drawable.setBounds(0, 0, DensityUtils.dip2px(getContext(), 30), DensityUtils.dip2px(getContext(), 30));
        tvLeft.setCompoundDrawables(null, drawable, null, null);
        tvLeft.setOnClickListener(onClickListener);
    }

    public void setRight(String text, @DrawableRes int drawableRes, View.OnClickListener onClickListener) {
        if (tvLeft == null) {
            return;
        }
        tvRight.setText(text);
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
        drawable.setBounds(0, 0, DensityUtils.dip2px(getContext(), 30), DensityUtils.dip2px(getContext(), 30));
        tvRight.setCompoundDrawables(null, drawable, null, null);
        tvRight.setOnClickListener(onClickListener);
    }
}
