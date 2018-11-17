package com.shosen.max.ui.activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.GetCountByUserIdBean;
import com.shosen.max.presenter.CircleUserMinePresenter;
import com.shosen.max.presenter.contract.CircleContract;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class CircleUserMineActivity extends BaseActivity implements CircleContract.UserInfoMineView {


    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_top_wrapper)
    RelativeLayout rlTopWrapper;
    @BindView(R.id.tv_focus_count)
    TextView tvFocusCount;
    @BindView(R.id.tv_focus_text)
    TextView tvFocusText;
    @BindView(R.id.tv_topic_count)
    TextView tvTopicCount;
    @BindView(R.id.tv_topic_text)
    TextView tvTopicText;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.tv_fans_text)
    TextView tvFansText;
    @BindView(R.id.fl_top_wrapper)
    FrameLayout flTopWrapper;
    @BindView(R.id.tv_my_message)
    TextView tvMyMessage;
    @BindView(R.id.tv_my_sc_message)
    TextView tvMyScMessage;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_my_message)
    RelativeLayout rlMyMessage;

    private CircleUserMinePresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this, this.getResources().getColor(R.color.seleted_text_color));
        mPresenter = new CircleUserMinePresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        if (LoginUtils.isLogin) {
            tvName.setText(LoginUtils.getUser().getName());
            GlideUtils.loadImage(this, LoginUtils.getUser().getHeadimg(), ivAvatar);
            mPresenter.getCountByUserId();
        }

    }

    @Override
    public void initTopHeader() {
        FrameLayout rlTop = (FrameLayout) findViewById(R.id.fl_top_wrapper);
        if (rlTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(this);
            rlTop.setLayoutParams(layoutParams);
        }
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_circle_mine;
    }

    @OnClick({R.id.iv_back, R.id.tv_focus_count, R.id.tv_focus_text, R.id.rl_my_message
            , R.id.tv_fans_count, R.id.tv_fans_text, R.id.tv_topic_count, R.id.tv_topic_text})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_focus_count:
            case R.id.tv_focus_text:
                intent = new Intent(this, FollowerListActivity.class);
                intent.putExtra("userId", LoginUtils.getUser().getUid());
                intent.putExtra("fromPage", FollowerListActivity.FROM_MY_FOCUS);
                startActivity(intent);
                break;
            case R.id.tv_topic_count:
            case R.id.tv_topic_text:
                ActivityUtils.startActivity(MyHotTopicActivity.class);
                break;
            case R.id.rl_my_message:
                intent = new Intent(this, MessageListActivity.class);
                intent.putExtra("fromPage", MessageListActivity.FROM_MY_CENTER);
                startActivity(intent);
                break;
            case R.id.tv_fans_count:
            case R.id.tv_fans_text:
                Intent intent2 = new Intent(this, FollowerListActivity.class);
                intent2.putExtra("userId", LoginUtils.getUser().getUid());
                intent2.putExtra("fromPage", FollowerListActivity.FROM_MY_FANS);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void showCountByUserId(GetCountByUserIdBean bean) {
        if (bean != null) {
            tvFansCount.setText(bean.getFansNum());
            tvFocusCount.setText(bean.getFollowNum());
            tvTopicCount.setText(bean.getMessageNum());
        }
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }
}
