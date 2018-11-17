package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.utils.ShareUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyInvitationActivity extends BaseActivity implements UMShareListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rl_invite_code_wrapper)
    RelativeLayout rlInviteCodeWrapper;
    @BindView(R.id.tv_invite)
    TextView tvInvite;

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("");
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_invitation;
    }

    @OnClick({R.id.iv_back, R.id.tv_invite})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_invite:
                //Test Only
                ShareUtils.shareMin(this, new UMImage(this, R.drawable.maxmaker_logo), "http://www.baidu.com",
                        "title", this);
                break;
            default:
                break;
        }
    }


    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //UM使用
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
