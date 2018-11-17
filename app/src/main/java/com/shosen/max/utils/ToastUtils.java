package com.shosen.max.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shosen.max.R;

/**
 * Toast工具类
 */
public class ToastUtils {
    private ToastUtils() {

    }

    private static Toast mToast;

    private static Toast alertToast;

    /**
     * Toast
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        //处理一些rom自带app name的问题
        mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    /**
     * 无网络
     */
    public static void showNoNet(Context context) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 取消
     */
    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static void showAlertToast(Context context, String message) {
        if (alertToast == null) {
            alertToast = new Toast(context);
            View view = View.inflate(context, R.layout.item_layout_toast, null);
            TextView tvAlert = (TextView) view.findViewById(R.id.tv_alert);
            tvAlert.setText(message);
            alertToast.setView(view);
            alertToast.setGravity(Gravity.CENTER, 0, 0);
            alertToast.setDuration(Toast.LENGTH_SHORT);
        } else {
            alertToast.cancel();
        }
        alertToast.show();
    }
}
