package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.SellerProductOrder;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class SellProductOrderDetailAdapter extends RecyclerView.Adapter<SellProductOrderDetailAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    List<SellerProductOrder.POrderItemListBean> itemList;
    public SellProductOrderDetailAdapter(Context context,List<SellerProductOrder.POrderItemListBean> itemList) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemList=itemList;
    }

    public void notifyDataSetChanged(List<SellerProductOrder.POrderItemListBean> itemList) {
        this.itemList = itemList;
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
        EditText edtMessage;
        LinearLayout llRemark;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_create_product_order_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivProduct = (ImageView) view.findViewById(R.id.iv_product);
        viewHolder.tvModel = (TextView) view.findViewById(R.id.tv_model);
        viewHolder.tvNum = (TextView) view.findViewById(R.id.tv_num);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
        viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
        viewHolder.llRemark = (LinearLayout) view.findViewById(R.id.ll_remark);
        viewHolder.edtMessage = (EditText) view.findViewById(R.id.edt_message);
        viewHolder.edtMessage.setFocusable(false);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int postion) {
        final SellerProductOrder.POrderItemListBean item = itemList.get(postion);
        GlideUtil.setImageToView(item.getPImage(), viewHolder.ivProduct);
        viewHolder.tvModel.setText(item.getPmTitle());
        viewHolder.tvName.setText(item.getPTitle());
        viewHolder.tvNum.setText(context.getString(R.string.multiply)
                .concat(String.valueOf(item.getPmCount())
                        .concat(item.getPUnit())));
        viewHolder.tvPrice.setText(context.getString(R.string.yuan)
                .concat(item.getPmPrice()+""));

        if (TextUtils.isEmpty(item.getRemark())) {
            viewHolder.llRemark.setVisibility(View.GONE);
        } else {
            //// FIXME: 2017/4/17/017 这里忘了显示
            viewHolder.llRemark.setVisibility(View.VISIBLE);
            viewHolder.edtMessage.setText(item.getRemark());
        }
//        viewHolder.edtMessage.setText(item.g);
        if (mOnItemClickLitener != null) {
            viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.ivProduct, postion);
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
