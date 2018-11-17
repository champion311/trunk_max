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
import android.widget.TextView;

import com.shosen.max.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DelOrderDialog extends Dialog {

    @BindView(R.id.certain_button)
    TextView certainButton;
    @BindView(R.id.cancel_button)
    TextView cancelButton;

    private DelButtonClick delButtonClick;

    public void setDelButtonClick(DelButtonClick delButtonClick) {
        this.delButtonClick = delButtonClick;
    }

    public DelOrderDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public DelOrderDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        //View view = View.inflate(getContext(), R.layout.dialog_cancel_order, null);
        window.setContentView(R.layout.dialog_del_order);
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

    @OnClick({R.id.certain_button, R.id.cancel_button})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.certain_button:
                if (delButtonClick != null) {
                    delButtonClick.delClick();
                }
                dismiss();
                break;
            case R.id.cancel_button:
                dismiss();
                break;
        }
    }

    public interface DelButtonClick {
        void delClick();
    }
}
