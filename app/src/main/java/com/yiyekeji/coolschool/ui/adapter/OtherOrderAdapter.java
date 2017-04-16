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
import com.yiyekeji.coolschool.bean.OtherOrder;
import com.yiyekeji.coolschool.utils.DateUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2017/1/23.
 */
public class OtherOrderAdapter extends RecyclerView.Adapter<OtherOrderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<OtherOrder> orderList;
    private Context context;

    public OtherOrderAdapter(Context context, List<OtherOrder> orderList) {
        this.orderList = orderList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<OtherOrder> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        TextView tvTitle;
        TextView tvState;
        TextView tvDate;
        TextView tvOrderType;
        LinearLayout llParent;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_other_order_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_date);
        viewHolder.tvOrderType=(TextView)view.findViewById(R.id.tv_orderType);
        viewHolder.tvState = (TextView) view.findViewById(R.id.tv_state);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        OtherOrder info = orderList.get(i);
        viewHolder.tvTitle.setText(info.getContactName().concat(info.getContactAddr()));
        if (info.getOrderState() == 1) {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.weixin_green));
        } else {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.theme_red));
        }
        viewHolder.tvDate.setText(DateUtils.subTimeFromMin(info.getOrderTime()));
        viewHolder.tvState.setText(info.getOrderState()==0?"待处理":"已完成");
        //订单类型 0 打印 2上门收件  1 代拿快递
        switch (info.getOrderType()) {
            case 0:
                viewHolder.tvOrderType.setText("打印");
                viewHolder.tvOrderType.setTextColor(ContextCompat.getColor(context,R.color.orange));
                break;
            case 1:
                viewHolder.tvOrderType.setText("代拿快递");
                viewHolder.tvOrderType.setTextColor(ContextCompat.getColor(context,R.color.theme_blue));
                break;
            case 2:
                viewHolder.tvOrderType.setText("上门收件");
                viewHolder.tvOrderType.setTextColor(ContextCompat.getColor(context,R.color.theme_purple));
                break;
        }
        if (mOnItemClickLitener != null) {
            viewHolder.llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v,i);
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
