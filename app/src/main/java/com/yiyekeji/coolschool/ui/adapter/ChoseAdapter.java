package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ChoseBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class ChoseAdapter extends RecyclerView.Adapter<ChoseAdapter.ViewHolder> {
    int type;
    int textColor = 0, backgroundColor = 0, defaultTextColot, defaultBackgroundColor;
    private LayoutInflater mInflater;
    private List<ChoseBean> beanList = new ArrayList<>();
    private Context context;

    public ChoseAdapter(Context context, List<ChoseBean> beanList) {
        this.beanList = beanList;
        mInflater = LayoutInflater.from(context);
        this.type = 0;
        this.context = context;
        textColor = ContextCompat.getColor(context, R.color.theme_red);
        defaultTextColot = ContextCompat.getColor(context, R.color.black);
    }

    public void notifyDataSetChanged(List<ChoseBean> beanList) {
        this.beanList = beanList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        TextView tvValue;
        ImageView ivSelected;
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_chosen_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvValue = (TextView) view.findViewById(R.id.tv_value);
        viewHolder.ivSelected = (ImageView) view.findViewById(R.id.iv_selected);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ChoseBean choseBean = beanList.get(i);
        viewHolder.tvValue.setText(choseBean.getValue().toString());
        viewHolder.ivSelected.setVisibility(View.INVISIBLE);
        if (choseBean.isSelect()) {
            viewHolder.ivSelected.setVisibility(View.VISIBLE);
        }
        if (mOnItemClickLitener != null) {
            viewHolder.tvValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (ChoseBean choseBean1 : beanList) {
                        choseBean1.setSelect(false);
                    }
                    beanList.get(i).setSelect(true);
                    notifyDataSetChanged();
                    mOnItemClickLitener.onItemClick(viewHolder.tvValue, i);
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
