package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.presenter.OrderDetailPresenter;
import com.shosen.max.presenter.contract.OrderDetailContract;
import com.shosen.max.utils.HDateUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CancelButtonClick;
import com.shosen.max.widget.dialog.CancelOrderDialog;
import com.shosen.max.widget.dialog.DelOrderDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity
        implements OrderDetailContract.View,
        CancelButtonClick, DelOrderDialog.DelButtonClick {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_order_image)
    ImageView ivOrderImage;
    @BindView(R.id.tv_order_info)
    TextView tvOrderInfo;
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_cancel_order)
    TextView tvCancelOrder;
    @BindView(R.id.tv_pay_continue)
    TextView tvPayContinue;
    @BindView(R.id.tv_del_order)
    TextView tvDelOrder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_cellphone)
    TextView tvCellphone;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    @BindView(R.id.ll_topay)
    LinearLayout llTopay;
    private OrderDetailPresenter mPresenter;

    private String bookId;

    private CancelOrderDialog cancelOrderDialog;

    private DelOrderDialog delOrderDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new OrderDetailPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        if (getIntent() != null) {
            bookId = getIntent().getStringExtra("bookId");
        }
        mPresenter.getOrderDetail(bookId);
        tvHeadTitle.setText("订单详情");

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_order_detail;
    }

    @OnClick({R.id.iv_back, R.id.tv_pay_continue, R.id.tv_cancel_order, R.id.tv_del_order})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_pay_continue:
                Intent intent = new Intent(this, PayActivity.class);
                intent.putExtra("bookId", bookId);
                startActivity(intent);
                break;
            case R.id.tv_cancel_order:
                if (cancelOrderDialog == null) {
                    cancelOrderDialog = new CancelOrderDialog(this, R.style.CancelDialog);
                    cancelOrderDialog.setCancelClick(this);
                }
                cancelOrderDialog.show();
                //mPresenter.cancelOrder(bookId, "233");
                break;
            case R.id.tv_del_order:
                //mPresenter.delOrder(bookId, "233");
                if (delOrderDialog == null) {
                    delOrderDialog = new DelOrderDialog(this, R.style.CancelDialog);
                    delOrderDialog.setDelButtonClick(this);
                }
                delOrderDialog.show();
        }

    }

    //BookStatus
    //1:接收预定2:己缴预定款3:己缴全款4:取消定单5:删除定单
    @Override
    public void showOrderDetails(OrderResponse response) {
        if (response == null) {
            return;
        }
        if ("2".equals(response.getBookStatus()) || "3".equals(response.getBookStatus())) {
            tvOrderState.setText("已完成");
            llTopay.setVisibility(View.GONE);
        } else if ("1".equals(response.getBookStatus())) {
            tvOrderState.setText("待支付");
            llTopay.setVisibility(View.VISIBLE);
            tvPayContinue.setVisibility(View.VISIBLE);
            tvCancelOrder.setVisibility(View.VISIBLE);
            tvDelOrder.setVisibility(View.GONE);
        } else if ("4".equals(response.getBookStatus())) {
            tvOrderState.setText("交易关闭");
            llTopay.setVisibility(View.VISIBLE);
            tvPayContinue.setVisibility(View.GONE);
            tvCancelOrder.setVisibility(View.GONE);
            tvDelOrder.setVisibility(View.VISIBLE);
        }
        //5已经删除
        tvName.setText(getString(R.string.name) + response.getBookUserName());
        tvCellphone.setText(getString(R.string.cell_phone) + "\t\t" + response.getBookUserPhone());
        tvZone.setText(getString(R.string.zone) + response.getProvince() + " " + response.getCity());
        tvOrderNumber.setText(getString(R.string.order_number) + response.getOrderNo());
        try {
            long time = Long.valueOf(response.getBookTime()) / 1000;
            tvOrderTime.setText(getString(R.string.order_time) + HDateUtils.getDate(time));
        } catch (NumberFormatException e) {

        }
        tvOrderPrice.setText(getString(R.string.orders_price) + response.getBookMoney());
    }

    @Override
    public void cancelOrderSuccess() {
        ToastUtils.showAlertToast(mContext, getResources().getString(R.string.cancel_order_success));
        mPresenter.getOrderDetail(bookId);
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void delOrderSuccess() {
        ToastUtils.showAlertToast(mContext, getResources().getString(R.string.del_order_success));
        //TODO 更新列表
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(mContext, message);
    }

    @Override
    public void cancelClick(String reason) {
        mPresenter.cancelOrder(bookId, reason);
    }

    @Override
    public void delClick() {
        mPresenter.delOrder(bookId, "");
    }
}
