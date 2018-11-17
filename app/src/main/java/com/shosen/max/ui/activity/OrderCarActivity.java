package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCarActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_pay_continue)
    TextView tvPayContinue;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("车辆配置");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_order_cars;
    }

    @OnClick({R.id.iv_back, R.id.tv_pay_continue})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_pay_continue:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(this, OrderConfirmActivity.class));
                break;
        }
    }
}
