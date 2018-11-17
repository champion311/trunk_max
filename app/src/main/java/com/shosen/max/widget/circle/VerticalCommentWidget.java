package com.shosen.max.widget.circle;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bin.david.form.utils.DensityUtils;
import com.shosen.max.R;
import com.shosen.max.bean.CommentBean;
import com.shosen.max.others.span.TextMovementMethod;
import com.shosen.max.ui.activity.circle.adapter.OnMenuItemClickListener;
import com.shosen.max.utils.LoginUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class VerticalCommentWidget extends LinearLayout implements
        ViewGroup.OnHierarchyChangeListener {

    private List<CommentBean> mCommentBeans;

    private LinearLayout.LayoutParams mLayoutParams;
    private SimpleWeakObjectPool<View> COMMENT_TEXT_POOL;
    private int mCommentVerticalSpace;
    private OnReplyClickListener onReplyClickListener;
    private OnMenuItemClickListener menuItemClickListener;

    public void setOnReplyClickListener(OnReplyClickListener onReplyClickListener) {
        this.onReplyClickListener = onReplyClickListener;
    }

    public void setMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }

    @Override
    protected void removeDetachedView(View child, boolean animate) {
        super.removeDetachedView(child, animate);
    }

    public VerticalCommentWidget(Context context) {
        super(context);
        init();
    }

    public VerticalCommentWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalCommentWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCommentVerticalSpace = DensityUtils.dp2px(getContext(), 5f);
        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<>();
        setOnHierarchyChangeListener(this);
    }

    public void addComments(List<CommentBean> commentBeans) {
        this.mCommentBeans = commentBeans;
        if (mCommentBeans == null || mCommentBeans.size() == 0) {
            return;
        }
        int oldCount = getChildCount();
        int newCount = commentBeans.size();
        if (oldCount > newCount) {
            removeViewsInLayout(newCount, oldCount - newCount);
        }
        for (int i = 0; i < newCount; i++) {
            boolean hasChild = i < oldCount;
            View childView = hasChild ? getChildAt(i) : null;
            CommentBean commentBean = commentBeans.get(i);
            commentBean.build(getContext());
            SpannableStringBuilder commentSpan = commentBean.getCommentContentSpan();
            if (childView == null) {
                childView = COMMENT_TEXT_POOL.get();
                if (childView == null) {
                    addViewInLayout(makeCommentItemView(commentSpan, i), i, generateMarginLayoutParams(i), true);
                } else {
                    addCommentItemView(childView, commentSpan, i);
                }
            } else {
                updateCommentData(childView, commentSpan, i);
            }
        }
        requestLayout();
    }

    /**
     * 添加需要的Comment View
     */
    private void addCommentItemView(View view, SpannableStringBuilder builder, int index) {
        addViewInLayout(makeCommentItemView(builder, index),
                index, generateMarginLayoutParams(index), true);
    }

    /**
     * 更新comment list content
     */
    private void updateCommentData(View view, SpannableStringBuilder builder, int index) {
        addViewInLayout(makeCommentItemView
                (builder, index), index, generateMarginLayoutParams(index), true);
        removeViewInLayout(view);

    }


    /**
     * 創建Comment item view
     */
    private TextView makeCommentItemView(SpannableStringBuilder content, int index) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.LEFT);
        textView.setText(content);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black_text_color));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selected_commment_bg_view));
        textView.setLineSpacing(mCommentVerticalSpace, 1f);
        //确定长按的区域
        textView.setMovementMethod(new TextMovementMethod());
        textView.setOnClickListener(v -> {
            if (onReplyClickListener != null) {
                onReplyClickListener.onReplyClick(textView, index, mCommentBeans.get(index));
            }
        });
        //设置长按的点击菜单
        if (mCommentBeans != null) {
            setOnMenuItemClick(textView, index, mCommentBeans.get(index));
        }
        return textView;
    }



    //TODO 待优化
    private void setOnMenuItemClick(View view, int position, CommentBean bean) {

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        if (LoginUtils.isLogin && LoginUtils.getUser().getUid().equals(bean.getUserId())) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_replys_mine, popupMenu.getMenu());
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.menu_replys, popupMenu.getMenu());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.copy:
                        if (menuItemClickListener != null) {
                            menuItemClickListener.onMenuCopyItemClickListener(view, position, bean);
                        }
                        break;
                    case R.id.del:
                        if (menuItemClickListener != null) {
                            menuItemClickListener.onMenuDelItemClickListener(view, position, bean);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        view.setOnLongClickListener(v -> {
            popupMenu.show();
            return false;
        });
    }


    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        COMMENT_TEXT_POOL.put(child);
    }

    private LayoutParams generateMarginLayoutParams(int index) {
        if (mLayoutParams == null) {
            mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mCommentBeans != null && index > 0) {
            //判断是否为最后一个元素
            mLayoutParams.bottomMargin = index == mCommentBeans.size() - 1 ? 0 : mCommentVerticalSpace;
        }
        return mLayoutParams;
    }

    /**
     * 回复点击监听
     */
    public interface OnReplyClickListener {
        void onReplyClick(View view, int position, CommentBean bean);
    }
}
