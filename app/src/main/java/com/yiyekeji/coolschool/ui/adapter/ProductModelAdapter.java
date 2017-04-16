package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class ProductModelAdapter extends RecyclerView.Adapter<ProductModelAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductModel> modelList;
    private Context context;

    public ProductModelAdapter(Context context, List<ProductModel> modelList) {
        this.modelList = modelList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ProductModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }

        EditText edtModel;
        EditText edtPrice;
        EditText edtBalance;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_modify_model, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.edtModel = (EditText) view.findViewById(R.id.edt_model);
        viewHolder.edtPrice = (EditText) view.findViewById(R.id.edt_price);
        viewHolder.edtBalance = (EditText) view.findViewById(R.id.edt_balance);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ProductModel productModel = modelList.get(i);
        viewHolder.edtModel.setText(productModel.getPmTitle());
        viewHolder.edtPrice.setText(productModel.getPmPrice());
        viewHolder.edtBalance.setText(productModel.getPmBalance() + "");

      /*  viewHolder.edtModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                modelList.get(i).setPmTitle(s.toString());
            }
        });

        viewHolder.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                modelList.get(i).setPmPrice(s.toString());
            }
        });

        viewHolder.edtBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    return;
                }
                modelList.get(i).setPmBalance(Integer.valueOf(s.toString()));
            }
        });*/

    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
