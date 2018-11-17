package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PartnerActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_partner_count)
    TextView tvPartnerCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("超级合伙人");
        setContentData();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_super_cooper;
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void setContentData() {
        Typeface customFort = Typeface.createFromAsset(getAssets(), "libian_sc2.ttf");
        tvPartnerCount.setTypeface(customFort);
        String text = "剩余1000个席位";
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                getResources().getColor(R.color.seleted_text_color));
        spannableString.setSpan(colorSpan, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPartnerCount.setText(spannableString);
    }
}
