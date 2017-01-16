package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CutClassNumInfo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class AbsenceStaticAdapter extends RecyclerView.Adapter<AbsenceStaticAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CutClassNumInfo> numInfoList;
    private  Context context;
    public AbsenceStaticAdapter(Context context, List<CutClassNumInfo> numInfoList) {
        this.numInfoList = numInfoList;
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void notifyDataSetChanged(List<CutClassNumInfo> numInfoList) {
        this.numInfoList=numInfoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }
        TextView tvLeft;
        TextView tvCenter;
        TextView tvRight;
        LinearLayout llContainer;
    }

    @Override
    public int getItemCount() {
        return numInfoList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_abstract_left_center_right_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llContainer=(LinearLayout)view.findViewById(R.id.ll_parent);
        viewHolder.tvLeft=(TextView)view.findViewById(R.id.tv_left);
        viewHolder.tvCenter=(TextView)view.findViewById(R.id.tv_center);
        viewHolder.tvRight=(TextView)view.findViewById(R.id.tv_right);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        CutClassNumInfo numInfo = numInfoList.get(i);

        viewHolder.tvLeft.setText(numInfo.getRealName());
        viewHolder.tvCenter.setText(numInfo.getUserNum());
        viewHolder.tvRight.setText(numInfo.getCount()+"次");
        if (mOnItemClickLitener != null) {
            viewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.llContainer, i);
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
