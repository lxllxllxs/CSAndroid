package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.ui.AbsenceListActivtiy;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class CourseInfoAdapter extends RecyclerView.Adapter<CourseInfoAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CourseInfo> courseInfoList;
    private  Context context;
    public CourseInfoAdapter(Context context, List<CourseInfo> courseInfoList) {
        this.courseInfoList = courseInfoList;
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void notifyDataSetChanged(List<CourseInfo> courseInfos) {
        this.courseInfoList=courseInfos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }

        TextView tvCourseName;
        TextView tvCout;
        TextView  tvClassNo;
        TextView  tvClass;
        TextView  tvCheck;
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
        View view = mInflater.inflate(R.layout.item_course_info_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llContainer=(LinearLayout)view.findViewById(R.id.ll_parent);
        viewHolder.tvCourseName=(TextView)view.findViewById(R.id.tv_courseName);
        viewHolder.tvClassNo=(TextView)view.findViewById(R.id.tv_classNo);
        viewHolder.tvCout=(TextView)view.findViewById(R.id.tv_count);
        viewHolder.tvClass=(TextView)view.findViewById(R.id.tv_class);
        viewHolder.tvCheck=(TextView)view.findViewById(R.id.tv_checkList);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final CourseInfo courseInfo = courseInfoList.get(i);

        String s=courseInfo.getCourseName()+"("+courseInfo.getCourseNo()+")";
        viewHolder.tvCourseName.setText(s);
        viewHolder.tvCout.setText(courseInfo.getCount().concat("人"));
        viewHolder.tvClass.setText(courseInfo.getCourseClass());
        viewHolder.tvClassNo.setText(courseInfo.getRoomNum());
        viewHolder.tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AbsenceListActivtiy.class);
                intent.putExtra("courseNo", courseInfo.getCourseNo());
                context.startActivity(intent);
            }
        });
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
