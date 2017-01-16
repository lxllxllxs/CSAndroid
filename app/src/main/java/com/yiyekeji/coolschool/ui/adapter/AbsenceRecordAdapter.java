package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ClassAbsenceInfo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class AbsenceRecordAdapter extends RecyclerView.Adapter<AbsenceRecordAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ClassAbsenceInfo> courseInfoList;
    private  Context context;
    public AbsenceRecordAdapter(Context context, List<ClassAbsenceInfo> courseInfoList) {
        this.courseInfoList = courseInfoList;
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void notifyDataSetChanged(List<ClassAbsenceInfo> courseInfos) {
        this.courseInfoList=courseInfos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }

        ImageView ivDel;
        TextView tvLeft;
        TextView tvCenter;

        TextView tvRight;
        LinearLayout llContainer;
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
        View view = mInflater.inflate(R.layout.item_abstract_left_center_right_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llContainer=(LinearLayout)view.findViewById(R.id.ll_parent);
        viewHolder.ivDel=(ImageView) view.findViewById(R.id.iv_del);
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
        ClassAbsenceInfo courseInfo = courseInfoList.get(i);

        if (isDeleteMode) {
            viewHolder.ivDel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivDel.setVisibility(View.GONE);
        }
        viewHolder.tvLeft.setText(courseInfo.getRealName());
        viewHolder.tvCenter.setText(courseInfo.getUserNum());
        viewHolder.tvRight.setText(courseInfo.getCutClassId()+"班");

        if (mOnItemClickLitener != null) {
            viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.ivDel, i);
                }
            });
        }


    }


    private boolean isDeleteMode = false;

    public void setDeleteMode(boolean b){
        this.isDeleteMode=b;
    }
    public boolean getDeleteMode(){
        return isDeleteMode;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
