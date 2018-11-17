package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.AlipayReqBean;
import com.shosen.max.bean.PayResult;
import com.shosen.max.bean.WxPayBean;
import com.shosen.max.bean.eventbusevent.WeChatPayEvent;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.PayPresenter;
import com.shosen.max.presenter.contract.PayContract;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseActivity implements PayContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.ra_wechat)
    RadioButton raWechat;
    @BindView(R.id.ra_alipay)
    RadioButton raAlipay;
    @BindView(R.id.rg_pay)
    RadioGroup rgPay;
    @BindView(R.id.tv_pay)
    TextView tvPay;

    private static final int ALIPAY = 0x100;
    private static final int WE_CHAT_PAY = 0x101;


    private PayPresenter mPresenter;
    /**
     * 订单id
     */
    private String bookId;

    private static final int ALIPAY_SDK_PAY_FLAG = 101;

    public static int payStatus;


    private IWXAPI iwxapi;
    /**
     * 处理支付宝支付结果
     */
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALIPAY_SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                        intent.putExtra("bookId", bookId);
                        startActivity(intent);
                        payStatus = Contstants.PAY_SUCCESS;
                        finish();


                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PayActivity.this, PayFailedActivity.class);
////                        intent.putExtra("bookId", bookId);
////                        startActivity(intent);
                        payStatus = Contstants.PAY_FAILED;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new PayPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initViewAndEvents() {
        payStatus = Contstants.PAY_NOT_YET;
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("确认支付");
        if (getIntent() != null) {
            bookId = getIntent().getStringExtra("bookId");
        }
        iwxapi = WXAPIFactory.createWXAPI(this, Contstants.WEI_XIN_APP_ID, true);
        iwxapi.registerApp(Contstants.WEI_XIN_APP_ID);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_confirm_pay;
    }


    @OnClick({R.id.iv_back, R.id.tv_pay})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                if (payStatus == Contstants.PAY_FAILED) {
                    ActivityUtils.finishActivity(OrderConfirmActivity.class);
                    ActivityUtils.finishActivity(OrderCarActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bookId", bookId);
                    ActivityUtils.startActivity(MyOrderActivity.class, bundle);
                    ActivityUtils.finishActivity(PayActivity.class);
                }
                finish();
                break;
            case R.id.tv_pay:
                //TODO 支付方式
                if (raAlipay.isChecked()) {
                    mPresenter.invokeAliPay(bookId);
                } else if (raWechat.isChecked()) {
                    mPresenter.invokeWxPay(bookId);
                }
                break;
            default:
                break;
        }
    }

    private ExecutorService singleThreadPool;

    public void invokeAlipay(String orderInfo) {
        // 修改为线程池模式
        singleThreadPool = new ThreadPoolExecutor(
                1, 1, 0,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = ALIPAY_SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        singleThreadPool.execute(payRunnable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (singleThreadPool != null) {
            singleThreadPool.shutdown();
        }

    }

    @Override
    public void invokeAliPayReqBack(AlipayReqBean reqResponse) {
        //调用支付宝支付
        invokeAlipay(reqResponse.getPayStr());
    }

    @Override
    public void invokeWxPayReqBack(WxPayBean wxBean) {
        //调用微信支付 在WXPayEntryActivity 处理回调
        PayReq payReq = new PayReq();
        payReq.appId = wxBean.getAppId();
        payReq.partnerId = wxBean.getPartnerId();
        payReq.prepayId = wxBean.getPrepayId();
        payReq.packageValue = wxBean.getPackAge();
        payReq.nonceStr = wxBean.getNonceStr();
        payReq.timeStamp = wxBean.getTimeStamp();
        payReq.sign = wxBean.getSign();
        iwxapi.sendReq(payReq);
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeChatPayEvent event) {
        //微信支付结果
        payStatus = event.getPayResStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (payStatus == Contstants.PAY_FAILED) {
            ActivityUtils.finishActivity(OrderConfirmActivity.class);
            ActivityUtils.finishActivity(OrderCarActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("bookId", bookId);
            ActivityUtils.startActivity(MyOrderActivity.class, bundle);
            ActivityUtils.finishActivity(PayActivity.class);
        }
        finish();
    }
}
