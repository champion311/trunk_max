package com.shosen.max.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class GridLayout extends ViewGroup {

    public static final int DEFAULT_COLUMN = 3;

    public static final int MAX_SELECTED_COUNT = 3;

    public GridLayout(Context context) {
        super(context);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int mColumnCount = DEFAULT_COLUMN;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        //高度测量模式
        int childHeightMeasureSpec = 0;
        //宽度测量模式
        int childWidthMeasureSpec = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childView.getMeasuredHeight(), MeasureSpec.EXACTLY);
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() / mColumnCount, MeasureSpec.EXACTLY);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int left = 0;
        int top = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (i == 0) {

            } else if (i % mColumnCount == 0) {
                left = 0;
                top += view.getMeasuredHeight();
            } else {
                left += view.getMeasuredWidth();
            }
            view.layout(left, top, left + view.getMeasuredWidth(), top + getMeasuredHeight());
        }
    }


}
