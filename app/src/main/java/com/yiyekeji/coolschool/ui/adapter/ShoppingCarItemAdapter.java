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
import com.yiyekeji.coolschool.bean.ShoppingCarProduct;
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
    ShoppingCarAdapter carAdapter;
    ShoppingCarProduct product;
    public ShoppingCarItemAdapter(Context context, ShoppingCarProduct shoppingCarProduct,ShoppingCarAdapter carAdapter) {
        this.productInfoList = shoppingCarProduct.getProductList();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.carAdapter=carAdapter;
        this.product=shoppingCarProduct;
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
        ImageView ivSelect;
        ImageView ivProduct;
        TextView tvName;
        TextView tvModel;
        TextView tvPrice;
        TextView tvUnti;
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
        viewHolder.ivSelect = (ImageView) view.findViewById(R.id.iv_select);
        viewHolder.ivProduct = (ImageView) view.findViewById(R.id.iv_product);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tvUnti = (TextView) view.findViewById(R.id.tv_unti);
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
       final ProductListBean productInfo = productInfoList.get(i);
        GlideUtil.setImageToView(productInfo.getImagePath(), viewHolder.ivProduct);
        viewHolder.ivSelect.setImageResource(R.mipmap.ic_no_select);
        if (productInfo.isSelect()) {
            viewHolder.ivSelect.setImageResource(R.mipmap.ic_selected);
        }
        viewHolder.tvName.setText(productInfo.getPTitle());
        viewHolder.tvModel.setText(productInfo.getPmTitle());
        final String price = String.valueOf(productInfo.getPmPrice());
        viewHolder.tvPrice.setText(context.getString(R.string.yuan).concat(price));
        viewHolder.tvUnti.setText("/"+productInfo.getPUnit());
        viewHolder.tvNum.setText(context.getString(R.string.multiply)+productInfo.getPmCount());
        viewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productInfo.isSelect()) {
                    viewHolder.ivSelect.setImageResource(R.mipmap.ic_selected);
                    productInfo.setSelect(true);
                } else {
                    viewHolder.ivSelect.setImageResource(R.mipmap.ic_no_select);
                    productInfo.setSelect(false);
                }
                checkSelectAll();
                carAdapter.mOnItemClickLitener.onItemClick(v,i);
                notifyDataSetChanged();
            }
        });
    }

    int count=0;
    int noSelectCount=0;
    //这里只是改变父adapter的ui
    public void checkSelectAll(){
        for (ProductListBean bean:productInfoList){
            if (bean.isSelect()){
                count++;
                continue;
            }
            noSelectCount++;
        }
        if (count == productInfoList.size()) {
            carAdapter.setSelectAll(true,product);
        }
        if ( noSelectCount == productInfoList.size()){
            carAdapter.setSelectAll(false,product);
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
