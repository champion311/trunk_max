package com.shosen.max.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MultiRadioGroup extends RadioGroup {

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public MultiRadioGroup(Context context) {
        super(context);
    }

    public MultiRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int currentCheckedId;

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof LinearLayout) {
            int childCount = ((LinearLayout) child).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = ((LinearLayout) child).getChildAt(i);
                if (view instanceof RadioButton) {
                    final RadioButton radioButton = (RadioButton) view;
                    radioButton.setOnTouchListener((v, event) -> {
                        radioButton.setChecked(true);
                        checkRadioButton(radioButton);
                        currentCheckedId = radioButton.getId();
                        if (mOnCheckedChangeListener != null) {
                            mOnCheckedChangeListener.onCheckedChanged(this, radioButton.getId());
                        }
                        return true;
                    });
                    if (radioButton.isChecked()) {
                        currentCheckedId = radioButton.getId();
                    }
                }
            }
        }


        super.addView(child, index, params);
    }


    private void checkRadioButton(RadioButton radioButton) {
        View child;
        int radioCount = getChildCount();
        for (int i = 0; i < radioCount; i++) {
            child = getChildAt(i);
            if (child instanceof RadioButton) {
                if (child == radioButton) {
                    // do nothing
                } else {
                    ((RadioButton) child).setChecked(false);
                }
            } else if (child instanceof LinearLayout) {
                int childCount = ((LinearLayout) child).getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View view = ((LinearLayout) child).getChildAt(j);
                    if (view instanceof RadioButton) {
                        final RadioButton button = (RadioButton) view;
                        if (button == radioButton) {
                            // do nothing
                        } else {
                            ((RadioButton) button).setChecked(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getCheckedRadioButtonId() {
        return currentCheckedId;
    }
}
