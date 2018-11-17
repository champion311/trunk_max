package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayFailedActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_look_order)
    TextView tvLookOrder;

    private String bookId;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("支付失败");
        if (getIntent() != null) {
            bookId = getIntent().getStringExtra("bookId");
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_pay_failed;
    }

    @OnClick({R.id.iv_back, R.id.tv_look_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_look_order:
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("bookId", bookId);
                startActivity(intent);
                break;
        }
    }
}
