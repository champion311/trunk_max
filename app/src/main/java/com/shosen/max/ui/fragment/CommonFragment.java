package com.shosen.max.ui.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.shosen.max.MaxApplication;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.StatusBarUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommonFragment extends BaseFragment {

    //@BindView(R.id.mWebView)
    WebView mWebView;
    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.ll_webview_wrapper)
    LinearLayout llWebviewWrapper;


    public CommonFragment() {
    }

    public static CommonFragment newInstance(String position) {
        Bundle bundle = new Bundle();
        bundle.putString("position", position);
        CommonFragment commonFragment = new CommonFragment();
        commonFragment.setArguments(bundle);
        return commonFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_common, null);
        unbinder = ButterKnife.bind(this, contentView);
        initTopHeader(contentView);
        return contentView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        StatusBarUtil.setLightMode(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), Color.WHITE);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
        ivBack.setVisibility(View.GONE);
        mWebView = new WebView(getActivity().getApplicationContext());
        mWebView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llWebviewWrapper.addView(mWebView, layoutParams);
        if (getArguments() != null) {
            String position = getArguments().getString("position");
            mWebView.setWebViewClient(new WebViewClient() {


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    mWebView.loadUrl(url);
                    if (mWebView.canGoBack()) {
                        ivBack.setVisibility(View.VISIBLE);
                    } else {
                        ivBack.setVisibility(View.GONE);
                    }
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (mWebView.canGoBack()) {
                        ivBack.setVisibility(View.VISIBLE);
                    } else {
                        ivBack.setVisibility(View.GONE);
                    }
                }
            });
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setAllowFileAccess(true);
//          mWebView.getSettings().setUseWideViewPort(true);//让webview读取网页设置的viewport，pc版网页
//          mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            //TODO 待修改
            File cacheFile = new File(MaxApplication.getAppContext().getCacheDir(), Contstants.WEB_VIEW_CACHE_FILE_NAME);
            mWebView.getSettings().setAppCachePath(cacheFile.getPath());
            if ("1".equals(position)) {
                mWebView.loadUrl(Contstants.MAX_URL);
                tvHeadTitle.setText("MAX资讯");
            } else if ("2".equals(position)) {
                mWebView.loadUrl(Contstants.CHARITY_URL);
                tvHeadTitle.setText("慈善");
            }
        }

    }

    public void initTopHeader(View view) {
        ViewGroup rlTop = (ViewGroup) view.findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.LayoutParams layoutParams = rlTop.getLayoutParams();
            if (layoutParams instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            } else if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            } else if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                ((CoordinatorLayout.LayoutParams) layoutParams).topMargin =
                        StatusBarUtil.getStatusBarHeight(getActivity());
            }
        }
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mWebView != null) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 处理内存泄露
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mWebView != null) {
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable t) {
            }
            mWebView = null;
        }
    }

    public void back() {
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                getActivity().finish();
            }
        }
    }
}
