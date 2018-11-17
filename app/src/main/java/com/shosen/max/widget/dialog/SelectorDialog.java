package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.ui.adapter.SelectorDialogAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectorDialog extends Dialog {

    @BindView(R.id.rc_list)
    RecyclerView rcList;


    public SelectorDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public void setAdapter(SelectorDialogAdapter mAdapter) {
        this.mAdapter = mAdapter;
        if (rcList != null) {
            rcList.setAdapter(mAdapter);
        }
    }

    private SelectorDialogAdapter mAdapter;


    public SelectorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    protected SelectorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        //View view = View.inflate(getContext(), R.layout.dialog_cancel_order, null);
        window.setContentView(R.layout.dialog_identity_selector);
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
        rcList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEventAndData();
    }

    public void initEventAndData() {

    }


    public interface SelectorClick {
        void selectorClick(DictionaryBean bean);
    }
}
