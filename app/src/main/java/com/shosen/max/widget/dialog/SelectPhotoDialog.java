package com.shosen.max.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.utils.TakePhotoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPhotoDialog extends Dialog {
    @BindView(R.id.tv_take_photo)
    TextView tvTakePhoto;
    @BindView(R.id.tv_album)
    TextView tvAlbum;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private SelectorDialogItemClickListener itemClickListener;

    public SelectPhotoDialog(@NonNull Context context) {
        super(context);
        initParams();
    }

    public SelectPhotoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initParams();
    }

    protected SelectPhotoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        //View view = View.inflate(getContext(), R.layout.dialog_cancel_order, null);
        window.setContentView(R.layout.dialog_select_photo);
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

    @OnClick({R.id.tv_take_photo, R.id.tv_album, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_take_photo:
                if (itemClickListener != null) {
                    itemClickListener.onDialogItemClick(view, view.getId());
                    dismiss();
                } else {
                    TakePhotoUtils.openCamera(getOwnerActivity());
                }
                break;
            case R.id.tv_album:
                if (itemClickListener != null) {
                    itemClickListener.onDialogItemClick(view, view.getId());
                    dismiss();
                } else {
                    TakePhotoUtils.openGallery(getOwnerActivity());
                }
                break;
            case R.id.tv_cancel:
                if (itemClickListener != null) {
                    itemClickListener.onDialogItemClick(view, view.getId());
                    dismiss();
                } else {
                    dismiss();
                }
                break;
        }
    }


    public void showTitle(boolean isShow) {
        if (tvTitle != null) {
            tvTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public interface SelectorDialogItemClickListener {
        void onDialogItemClick(View view, int id);
    }

    public void setItemClickListener(SelectorDialogItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
