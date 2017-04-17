package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CancelOrder;
import com.yiyekeji.coolschool.bean.CourseAbsenceInfo;
import com.yiyekeji.coolschool.utils.DateUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CancelOrder> cancelOrderList;
    private  Context context;
    public CancelOrderAdapter(Context context, List<CancelOrder> cancelOrderList) {
        this.cancelOrderList = cancelOrderList;
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void notifyDataSetChanged(List<CancelOrder> cancelOrderList) {
        this.cancelOrderList=cancelOrderList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }

        TextView tvMain;
        TextView tvSecondary;
        TextView tvState;
        LinearLayout llContainer;
    }

    @Override
    public int getItemCount() {
        return cancelOrderList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_cancel_order_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llContainer=(LinearLayout)view.findViewById(R.id.ll_parent);
        viewHolder.tvMain=(TextView)view.findViewById(R.id.tv_main);
        viewHolder.tvSecondary=(TextView)view.findViewById(R.id.tv_secondary);
        viewHolder.tvState = (TextView) view.findViewById(R.id.tv_state);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        CancelOrder cancelOrder = cancelOrderList.get(i);
        viewHolder.tvMain.setText(cancelOrder.getRealName());
        viewHolder.tvSecondary.setText(cancelOrder.getAddress());

        //0是以处理的 1是等待处理的
        switch (cancelOrder.getStatus()){
            case "0":
                viewHolder.tvState.setText("已处理");
                viewHolder.tvState.setTextColor(ContextCompat.getColor(context,R.color.weixin_green));
                break;
            case "1":
                viewHolder.tvState.setText("退货中");
                viewHolder.tvState.setTextColor(ContextCompat.getColor(context,R.color.theme_red));
                break;
            default:
                viewHolder.tvState.setText("退货中");
                viewHolder.tvState.setTextColor(ContextCompat.getColor(context,R.color.theme_red));
                break;

        }
        if (mOnItemClickLitener != null) {
            viewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.llContainer, i);
                }
            });
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
