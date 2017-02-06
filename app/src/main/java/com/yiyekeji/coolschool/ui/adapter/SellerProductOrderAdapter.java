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
import com.yiyekeji.coolschool.bean.ProductOrder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2017/1/23.
 */
public class SellerProductOrderAdapter extends RecyclerView.Adapter<SellerProductOrderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductOrder> productInfos;
    private Context context;

    public SellerProductOrderAdapter(Context context, List<ProductOrder> productInfos) {
        this.productInfos = productInfos;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ProductOrder> productInfos) {
        this.productInfos = productInfos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        TextView tvProductName;
        TextView tvDate;
        TextView tvState;
        LinearLayout llParent;
    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_sell_product_order_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        viewHolder.tvProductName = (TextView) view.findViewById(R.id.tv_productName);
        viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_date);
        viewHolder.tvState = (TextView) view.findViewById(R.id.tv_state);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ProductOrder info = productInfos.get(i);
        viewHolder.tvProductName.setText(info.getpTitle());
        viewHolder.tvDate.setText(info.getOrderTime());
        viewHolder.tvState.setText(info.getPoState()==0?"待送货":"已完成");
        if (info.getPoState() == 1) {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.weixin_green));
        } else {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.theme_red));
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
