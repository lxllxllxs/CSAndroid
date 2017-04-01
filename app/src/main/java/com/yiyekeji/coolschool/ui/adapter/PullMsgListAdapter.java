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
import com.yiyekeji.coolschool.dao.PullMsg;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class PullMsgListAdapter extends RecyclerView.Adapter<PullMsgListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<PullMsg> pullMsgList;
    private  Context context;
    public PullMsgListAdapter(Context context, List<PullMsg> pullMsgList) {
        this.pullMsgList = pullMsgList;
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public void notifyDataSetChanged(List<PullMsg> pullMsgList) {
        this.pullMsgList=pullMsgList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }
        TextView tvMain;
        TextView tvDate;
        ImageView ivIsRead;
        LinearLayout llContainer;
    }

    @Override
    public int getItemCount() {
        return pullMsgList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_pull_msg_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.llContainer=(LinearLayout)view.findViewById(R.id.ll_parent);
        viewHolder.tvMain=(TextView)view.findViewById(R.id.tv_content);
        viewHolder.tvDate=(TextView)view.findViewById(R.id.tv_date);
        viewHolder.ivIsRead = (ImageView) view.findViewById(R.id.iv_isRead);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        PullMsg  pullMsg = pullMsgList.get(i);
        viewHolder.tvMain.setText(pullMsg.getContent());
        viewHolder.tvDate.setText(pullMsg.getDate());
        if (pullMsg.getIsRead() != 1) {
            viewHolder.ivIsRead.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivIsRead.setVisibility(View.INVISIBLE);
        }
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
