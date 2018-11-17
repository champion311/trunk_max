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

public class NotLoginActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_quit_login)
    TextView tvQuitLogin;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.item_not_login;
    }

    @OnClick({R.id.iv_back, R.id.tv_quit_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_quit_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


}
