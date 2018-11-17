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

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageGridAdapter extends RecyclerView.Adapter<HomePageGridAdapter.ViewHolder> {


    private Context mContext;

    private String[] arrayData;

    private HomePageGridOnClick mInterface;

    public void setInterface(HomePageGridOnClick mInterface) {
        this.mInterface = mInterface;
    }

    public HomePageGridAdapter(Context mContext) {
        arrayData = mContext.getResources().getStringArray(R.array.grid_array);
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.item_gridlayout, viewGroup, false);
        View view = View.inflate(mContext, R.layout.item_gridlayout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        switch (position) {
            case 0:
                viewHolder.gridIcon.setImageResource(R.drawable.activity_icon);
                break;
            case 1:
                viewHolder.gridIcon.setImageResource(R.drawable.sign_in_icon);
                break;
            case 2:
                viewHolder.gridIcon.setImageResource(R.drawable.invite_icon);
                break;
            case 3:
                viewHolder.gridIcon.setImageResource(R.drawable.mail_icon);
                break;

        }
        viewHolder.gridDes.setText(arrayData[position]);
        if (mInterface != null) {
            viewHolder.itemView.setOnClickListener(v -> {
                mInterface.OnGridClick(v, position);
            });
        }


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grid_icon)
        ImageView gridIcon;
        @BindView(R.id.grid_des)
        TextView gridDes;
        private Context mContext;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface HomePageGridOnClick {
        void OnGridClick(View view, int position);
    }


}
