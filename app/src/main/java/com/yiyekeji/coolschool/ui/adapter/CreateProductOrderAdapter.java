package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductOrderItem;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class CreateProductOrderAdapter extends RecyclerView.Adapter<CreateProductOrderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    List<ProductOrderItem> itemList;

    public CreateProductOrderAdapter(Context context, List<ProductOrderItem> itemList) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemList=itemList;
    }

    public void notifyDataSetChanged(List<ProductOrderItem> itemList) {
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

        viewHolder.edtMessage = (EditText) view.findViewById(R.id.edt_message);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int postion) {
        final ProductOrderItem item = itemList.get(postion);
        GlideUtil.setImageToView(item.getImgPath(), viewHolder.ivProduct);
        viewHolder.tvModel.setText(item.getmTitle());
        viewHolder.tvName.setText(item.getProductName());
        viewHolder.tvNum.setText(context.getString(R.string.multiply)
                .concat(String.valueOf(item.getPmCount())
                        .concat(item.getUnit())));
        viewHolder.tvPrice.setText(context.getString(R.string.yuan)
                .concat(item.getPrice()));

        if (mOnItemClickLitener != null) {
            viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.ivProduct, postion);
                }
            });
        }

        viewHolder.edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
           /*     if (TextUtils.isEmpty(s)){
                    return;
                }*/
                itemList.get(postion).setMessage(s.toString());
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
