package com.shosen.max.others.span;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.shosen.max.R;
import com.shosen.max.utils.ToastUtils;

import io.reactivex.annotations.NonNull;

public class TextClickSpan extends ClickableSpan {

    private Context mContext;

    private String mUserName;
    private boolean mPressed;

    public TextClickSpan(Context context, String userName) {
        this.mContext = context;
        this.mUserName = userName;
    }

    public void setPressed(boolean isPressed) {
        this.mPressed = isPressed;
    }

    @Override
    public void onClick(@NonNull View widget) {
        ToastUtils.show(mContext, mUserName);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.bgColor = mPressed ? ContextCompat.getColor(mContext, R.color.base_B5B5B5) : Color.TRANSPARENT;
        ds.setColor(ContextCompat.getColor(mContext, R.color.c666));
        ds.setUnderlineText(false);
    }
}
