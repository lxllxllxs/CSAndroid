package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.inter.HaveName;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class HaveNameAdapter extends RecyclerView.Adapter<HaveNameAdapter.ViewHolder> {
    int type;
    boolean needChanageStatus;
    int textColor = 0, backgroundColor = 0, defaultTextColot, defaultBackgroundColor;
    private LayoutInflater mInflater;
    private List<HaveName> keyWordNewsList = new ArrayList<>();
    private Context context;

    public HaveNameAdapter(Context context, List<HaveName> keyWordNewsList) {
        this.keyWordNewsList = keyWordNewsList;
        mInflater = LayoutInflater.from(context);
        this.type =0;
        this.context = context;
    }

    public void setSelectColor(int textColor, int backgroundColor) {
        needChanageStatus = true;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public HaveNameAdapter(Context context, List<HaveName> keyWordNewsList, int type) {
        this.keyWordNewsList = keyWordNewsList;
        mInflater = LayoutInflater.from(context);
        this.type = type;
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
        return keyWordNewsList.size();
    }

    /**
     * 默认为0 左对齐 有右箭头
     * 1 有样式
     * 2 无样式 居中
     * 3 无样式 左对齐 无箭头
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
        switch (type) {
            case 0:
                view = mInflater.inflate(R.layout.item_havename_center_adapter, viewGroup, false);
                break;
            case 1:
                break;
            case 2:

                break;
            case 3:
                break;
            default:
                break;
        }
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_names = (TextView) view.findViewById(R.id.tv_name);
        defaultTextColot = viewHolder.tv_names.getCurrentTextColor();
        defaultBackgroundColor = viewHolder.tv_names.getDrawingCacheBackgroundColor();
        return viewHolder;
    }

    public void setGravity() {

    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HaveName keyWord = keyWordNewsList.get(i);
        viewHolder.tv_names.setText(keyWord.getName());

        if (needChanageStatus) {
            viewHolder.tv_names.setTextColor(defaultTextColot);
            viewHolder.tv_names.setBackgroundColor(defaultBackgroundColor);
            if (keyWord.isSelect()) {
                viewHolder.tv_names.setTextColor(textColor);
                viewHolder.tv_names.setBackgroundColor(backgroundColor);
            }
        }
        if (mOnItemClickLitener != null) {
            viewHolder.tv_names.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //只有设置了状态才会
                    if (needChanageStatus) {
                        for (HaveName haveName : keyWordNewsList) {
                            haveName.setSelect(false);
                        }
                        keyWordNewsList.get(i).setSelect(true);
                        notifyDataSetChanged();
                    }
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


    public void switchData(List<HaveName> list) {
        this.keyWordNewsList = list;
        notifyDataSetChanged();
    }

}
