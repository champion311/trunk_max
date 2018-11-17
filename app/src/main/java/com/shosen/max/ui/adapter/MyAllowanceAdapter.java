package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.utils.HDateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAllowanceAdapter extends RecyclerView.Adapter<MyAllowanceAdapter.ViewHolder> {


    @BindView(R.id.tv_car_owner_counts)
    TextView tvCarOwnerCounts;
    @BindView(R.id.tv_car_invite_date)
    TextView tvCarInviteDate;
    @BindView(R.id.tv_get_money)
    TextView tvGetMoney;
    private Context mContext;

    private List<RewardListBean> mData;


    public MyAllowanceAdapter(Context mContext, List<RewardListBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_allowance_details, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RewardListBean bean = mData.get(i);
        if (bean == null) {
            return;
        }
        long time = Long.valueOf(bean.getUpdateTime());
        viewHolder.tvCarOwnerCounts.setText("累计邀请" + bean.getInvitSum() + "位车主");
        viewHolder.tvCarInviteDate.setText(HDateUtils.getDate(time));
        viewHolder.tvGetMoney.setText(bean.getReward());


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_car_owner_counts)
        TextView tvCarOwnerCounts;
        @BindView(R.id.tv_car_invite_date)
        TextView tvCarInviteDate;
        @BindView(R.id.tv_get_money)
        TextView tvGetMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
