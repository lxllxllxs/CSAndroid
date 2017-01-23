package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.inter.HaveName;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectModelAdapter extends RecyclerView.Adapter<SelectModelAdapter.ViewHolder> {
    int type;
    boolean needChanageStatus;
    int textColor = 0, backgroundColor = 0, defaultTextColot, defaultBackgroundColor;
    private LayoutInflater mInflater;
    private List<ProductModel> modelList = new ArrayList<>();
    private Context context;

    public SelectModelAdapter(Context context, List<ProductModel> modelList) {
        this.modelList = modelList;
        mInflater = LayoutInflater.from(context);
        this.type =0;
        this.context = context;
        textColor= ContextCompat.getColor(context,R.color.theme_red);
        defaultTextColot= ContextCompat.getColor(context,R.color.black);
    }

    public void notifyDataSetChanged(List<ProductModel> modelList) {
        this.modelList=modelList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        TextView tv_names;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View  view = mInflater.inflate(R.layout.item_havename_24center_adapter, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_names = (TextView) view.findViewById(R.id.tv_name);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ProductModel model = modelList.get(i);
        viewHolder.tv_names.setText(model.getName());

        viewHolder.tv_names.setTextColor(defaultTextColot);
        viewHolder.tv_names.setBackgroundResource(R.drawable.bg_0radiu_whitesolid_graytroke);
        if (model.isSelect()) {
            viewHolder.tv_names.setTextColor(textColor);
            viewHolder.tv_names.setBackgroundResource(R.drawable.bg_0radiu_whitesolid_redstroke);
        }
        if (mOnItemClickLitener != null) {
            viewHolder.tv_names.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (HaveName haveName : modelList) {
                        haveName.setSelect(false);
                    }
                    modelList.get(i).setSelect(true);
                    notifyDataSetChanged();
                    mOnItemClickLitener.onItemClick(viewHolder.tv_names, i);
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
