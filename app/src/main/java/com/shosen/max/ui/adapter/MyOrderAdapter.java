package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.OrderResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {


    private Context mContext;

    private List<OrderResponse> mList;

    private MyOrderItemClick onItemClick;

    public void setOnItemClick(MyOrderItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MyOrderAdapter(Context mContext, List<OrderResponse> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order, viewGroup, false);
        return new ViewHolder(view);
    }

    //1:接收预定2:己缴预定款3:己缴全款4:取消定单5:删除定单'
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        OrderResponse response = mList.get(position);
        if (response != null) {
            viewHolder.tvOrderPrice.setText(response.getBookMoney());
            if ("1".equals(response.getBookStatus())) {
                viewHolder.tvImmePay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvImmePay.setVisibility(View.GONE);
            }

        }
        if (onItemClick != null) {
            viewHolder.itemView.setOnClickListener(v -> {
                onItemClick.OnMyOrderItemClick(position, v, response);
            });
        }
        if ("1".equals(response.getBookStatus())) {
            viewHolder.tvOrderState.setText("待支付");
        } else if ("2".equals(response.getBookStatus()) || "3".equals(response.getBookStatus())) {
            viewHolder.tvOrderState.setText("已完成");
        } else if ("4".equals(response.getBookStatus())) {
            viewHolder.tvOrderState.setText("交易关闭");
        }


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_order_image)
        ImageView ivOrderImage;
        @BindView(R.id.tv_order_price)
        TextView tvOrderPrice;
        @BindView(R.id.tv_imme_pay)
        TextView tvImmePay;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface MyOrderItemClick {
        void OnMyOrderItemClick(int position, View view, OrderResponse response);
    }
}
