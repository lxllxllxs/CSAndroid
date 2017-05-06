package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.TuCaoComment;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class TuCaoCommentAdapter extends RecyclerView.Adapter<TuCaoCommentAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<TuCaoComment> commentList;
    private Context context;

    public TuCaoCommentAdapter(Context context, List<TuCaoComment> commentList) {
        this.commentList = commentList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<TuCaoComment> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_tucao_comment_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_content);
        viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_date);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        TuCaoComment comment = commentList.get(i);
        viewHolder.tvContent.setText(comment.getContent());
//         FIXME: 2017/4/23 这里以后改昵称
        viewHolder.tvName.setText(comment.getAuthor());
        viewHolder.tvDate.setText(comment.getDate().substring(5, 16));
    }



}
