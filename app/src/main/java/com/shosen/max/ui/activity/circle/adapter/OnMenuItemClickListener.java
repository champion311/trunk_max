package com.shosen.max.ui.activity.circle.adapter;

import android.view.View;

import com.shosen.max.bean.CommentBean;

/**
 * 菜单点击监听
 */
public interface OnMenuItemClickListener {

    void onMenuCopyItemClickListener(View view, int position, CommentBean bean);

    void onMenuDelItemClickListener(View view, int position, CommentBean bean);
}
