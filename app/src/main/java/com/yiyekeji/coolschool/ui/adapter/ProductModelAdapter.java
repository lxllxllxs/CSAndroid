package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class ProductModelAdapter extends RecyclerView.Adapter<ProductModelAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductModel> courseInfoList;
    private Context context;

    public ProductModelAdapter(Context context, List<ProductModel> courseInfoList) {
        this.courseInfoList = courseInfoList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ProductModel> courseInfos) {
        this.courseInfoList = courseInfos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }
        LableEditView ledtModel;
        LableEditView ledtPrice;
        LableEditView ledtStock;
    }

    @Override
    public int getItemCount() {
        return courseInfoList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_modify_model, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ledtModel = (LableEditView) view.findViewById(R.id.ledt_model);
        viewHolder.ledtPrice = (LableEditView) view.findViewById(R.id.ledt_price);
        viewHolder.ledtStock = (LableEditView) view.findViewById(R.id.ledt_stock);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ProductModel productModel = courseInfoList.get(i);
        viewHolder.ledtModel.setEditText(productModel.getPmTitle());
        viewHolder.ledtPrice.setEditText(productModel.getPmPrice()+"");
        viewHolder.ledtStock.setEditText(productModel.getPmBalance()+"");
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
