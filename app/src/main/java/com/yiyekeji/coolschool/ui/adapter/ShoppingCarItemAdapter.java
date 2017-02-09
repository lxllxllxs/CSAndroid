package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductListBean;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class ShoppingCarItemAdapter extends RecyclerView.Adapter<ShoppingCarItemAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductListBean> productInfoList;
    private Context context;

    public ShoppingCarItemAdapter(Context context, List<ProductListBean> productInfoList) {
        this.productInfoList = productInfoList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ProductListBean> productInfoList) {
        this.productInfoList = productInfoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        ImageView ivProduct;
        TextView tvName;
        TextView tvModel;
        TextView tvPrice;
        TextView tvNum;
    }

    @Override
    public int getItemCount() {
        return productInfoList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_shopping_car_item_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivProduct = (ImageView) view.findViewById(R.id.iv_product);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tvModel = (TextView) view.findViewById(R.id.tv_model);
        viewHolder.tvNum = (TextView) view.findViewById(R.id.tv_num);
        viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ProductListBean productInfo = productInfoList.get(i);
        GlideUtil.setImageToView(productInfo.getImagePath(), viewHolder.ivProduct);
        viewHolder.tvName.setText(productInfo.getPTitle());
        String price = String.valueOf(productInfo.getPmCount());
        viewHolder.tvPrice.setText(context.getString(R.string.yuan).concat(price));
        if (mOnItemClickLitener != null) {
            viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.ivProduct, i);
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
