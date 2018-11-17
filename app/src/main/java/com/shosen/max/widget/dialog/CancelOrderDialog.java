package com.shosen.max.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelOrderDialog extends Dialog {


    @BindView(R.id.tv_cancel_order)
    TextView tvCancelOrder;
    @BindView(R.id.tv_pay_continue)
    TextView tvPayContinue;
    @BindView(R.id.rg_reason)
    RadioGroup rgReason;

    private CancelButtonClick mCancelClick;

    public void setCancelClick(CancelButtonClick mCancelClick) {
        this.mCancelClick = mCancelClick;
    }

    public CancelOrderDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public CancelOrderDialog(Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        //View view = View.inflate(getContext(), R.layout.dialog_cancel_order, null);
        window.setContentView(R.layout.dialog_cancel_order);
        ButterKnife.bind(this);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = ScreenUtils.getScreenWidth(getContext());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEventAndData();
    }

    public void initEventAndData() {


    }

    @OnClick({R.id.tv_cancel_order, R.id.tv_pay_continue})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_order:
                if (mCancelClick != null) {
                    mCancelClick.cancelClick(getCheckedReason());
                }
                dismiss();
                break;
            case R.id.tv_pay_continue:
                dismiss();
                break;
        }
    }

    public String getCheckedReason() {
        int id = rgReason.getCheckedRadioButtonId();
        RadioButton button = findViewById(id);
        return button.getText().toString().trim();
    }


}
