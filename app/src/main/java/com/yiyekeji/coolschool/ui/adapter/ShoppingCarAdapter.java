package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductListBean;
import com.yiyekeji.coolschool.bean.ShoppingCarProduct;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class ShoppingCarAdapter extends RecyclerView.Adapter<ShoppingCarAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ShoppingCarProduct> productInfoList;
    private Context context;

    public ShoppingCarAdapter(Context context, List<ShoppingCarProduct> productInfoList) {
        this.productInfoList = productInfoList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ShoppingCarProduct> productInfoList) {
        this.productInfoList = productInfoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }

        ImageView ivSelect;
        TextView tvName;
        RecyclerView rvBlock;
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
        View view = mInflater.inflate(R.layout.item_shopping_car_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivSelect = (ImageView) view.findViewById(R.id.iv_select);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.rvBlock = (RecyclerView) view.findViewById(R.id.rv_productBlock);
        viewHolder.rvBlock.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvBlock.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final ShoppingCarProduct productInfo = productInfoList.get(i);
        final ShoppingCarItemAdapter itemAdapter = new ShoppingCarItemAdapter(context, productInfo,this);
        viewHolder.ivSelect.setImageResource(R.mipmap.ic_no_select);
        if (productInfo.isSelect()) {
            viewHolder.ivSelect.setImageResource(R.mipmap.ic_selected);
        }
        viewHolder.rvBlock.setAdapter(itemAdapter);
        viewHolder.tvName.setText(productInfo.getSupplierName() + "  " + productInfo.getSupplierPhone());
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
                for (ProductListBean product:productInfoList.get(i).getProductList()){
                    product.setSelect(productInfo.isSelect());
                }
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(v,i);
                }
                itemAdapter.notifyDataSetChanged(productInfoList.get(i).getProductList());
                notifyDataSetChanged();
            }
        });
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private  void notifyDataSetChangede(){

    }

    public  void  setSelectAll(boolean b,ShoppingCarProduct product){
        product.setSelect(b);
        notifyDataSetChanged();
    }
    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
