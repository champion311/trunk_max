package com.shosen.max.widget.circle.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.ScreenUtils;

public class BottomInsertPop extends PopupWindow {

    public BottomInsertPop(Context context) {
        View view = View.inflate(context, R.layout.pop_bottom_comment_insert, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(DensityUtils.dip2px(context, 80));
        this.setFocusable(true);
        this.update();
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }

    public void focusEditText(TextView.OnEditorActionListener editorActionListener) {
        EditText edComment = (EditText) getContentView().findViewById(R.id.ed_comment);
        if (edComment != null) {
            edComment.setFocusable(true);
            edComment.setFocusableInTouchMode(true);
            edComment.requestFocus();
            edComment.setOnEditorActionListener(editorActionListener);
        }
    }

    public EditText getEditText() {
        return (EditText) getContentView().findViewById(R.id.ed_comment);
    }

}
