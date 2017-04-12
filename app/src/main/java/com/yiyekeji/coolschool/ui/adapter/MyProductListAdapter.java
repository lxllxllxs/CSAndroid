package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.ui.EditProductAyt;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2017/1/23.
 */
public class MyProductListAdapter extends RecyclerView.Adapter<MyProductListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductInfo> productInfos;
    private Context context;

    public MyProductListAdapter(Context context, List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        TextView tvProductName;
        TextView tvModify;
        TextView tvDel;
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
        View view = mInflater.inflate(R.layout.item_modify_product_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvProductName = (TextView) view.findViewById(R.id.tv_productName);
        viewHolder.tvModify = (TextView) view.findViewById(R.id.tv_modify);
        viewHolder.tvDel= (TextView) view.findViewById(R.id.tv_del);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final ProductInfo info = productInfos.get(i);
        viewHolder.tvProductName.setText(info.getpTitle());
        if (info.getpState() == 1) {
            viewHolder.tvDel.setText("下架");
            viewHolder.tvDel.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        } else {
            viewHolder.tvDel.setText("上架");
            viewHolder.tvDel.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_green));
        }

        //接入 修改产品
        viewHolder.tvModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProductAyt.class);
                intent.putExtra("pId",info.getPid());
                context.startActivity(intent);
            }
        });
        if (mDelClickLitener != null) {
            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDelClickLitener.onItemClick(v, i);
                }
            });
        }

        if (mOnItemClickLitener != null) {
            viewHolder.tvProductName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v, i);
                }
            });
        }

    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener,mDelClickLitener;

    public void setmDelClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mDelClickLitener = mOnItemClickLitener;
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
