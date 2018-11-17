package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.widget.MultiRadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportReasonDialog extends Dialog {


    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;
    @BindView(R.id.rb_5)
    RadioButton rb5;
    @BindView(R.id.rg_reason)
    MultiRadioGroup rgReason;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    private CancelButtonClick mCancelClick;

    public ReportReasonDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public ReportReasonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        window.setContentView(R.layout.dialog_report_reason);
        ButterKnife.bind(this);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = ScreenUtils.getScreenWidth(getContext());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setCanceledOnTouchOutside(true);
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.tv_complete})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_complete:
                if (mCancelClick != null) {
                    mCancelClick.cancelClick(getCheckedReason());
                }
                dismiss();
                break;
        }
    }

    public String getCheckedReason() {
        int id = rgReason.getCheckedRadioButtonId();
        RadioButton button = findViewById(id);
        return button.getText().toString().trim();
    }

    public void setCancelClick(CancelButtonClick mCancelClick) {
        this.mCancelClick = mCancelClick;
    }
}
