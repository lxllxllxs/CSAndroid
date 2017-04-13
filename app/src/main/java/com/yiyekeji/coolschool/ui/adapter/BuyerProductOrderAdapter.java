package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductOrder;
import com.yiyekeji.coolschool.ui.CreateReturnOrderAty;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2017/1/23.
 */
public class BuyerProductOrderAdapter extends RecyclerView.Adapter<BuyerProductOrderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductOrder> productInfos;
    private Context context;

    public BuyerProductOrderAdapter(Context context, List<ProductOrder> productInfos) {
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
        TextView tvState;
        TextView tvReject;
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
        View view = mInflater.inflate(R.layout.item_buy_product_order_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        viewHolder.tvProductName = (TextView) view.findViewById(R.id.tv_productName);
        viewHolder.tvState = (TextView) view.findViewById(R.id.tv_state);
        viewHolder.tvReject = (TextView) view.findViewById(R.id.tv_reject);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     *
     * 0是待送货
     * 1是完成送货
     * 2是退货中
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final ProductOrder info = productInfos.get(i);
        viewHolder.tvProductName.setText(info.getpTitle());
        if (info.getPoState() == 1) {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.weixin_green));
            viewHolder.tvReject.setVisibility(View.VISIBLE);
        } else if (info.getPoState() == 0) {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.theme_red));
            viewHolder.tvReject.setVisibility(View.GONE);
        } else {
            viewHolder.tvState.setTextColor(ContextCompat.getColor(context, R.color.theme_red));
            viewHolder.tvReject.setVisibility(View.GONE);
        }
        switch (info.getPoState()){
            case  0:
                viewHolder.tvState.setText("待送货");
                break;
            case  1:
                viewHolder.tvState.setText("已完成");
                break;
            case  2:
                viewHolder.tvState.setText("退货中");
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

        viewHolder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateReturnOrderAty.class);
                intent.putExtra("info", info);
                context.startActivity(intent);
            }
        });
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
