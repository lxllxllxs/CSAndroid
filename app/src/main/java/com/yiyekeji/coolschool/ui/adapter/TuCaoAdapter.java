package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class TuCaoAdapter extends RecyclerView.Adapter<TuCaoAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<TuCao> tuCaoList;
    private Context context;

    public TuCaoAdapter(Context context, List<TuCao> tuCaoList) {
        this.tuCaoList = tuCaoList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<TuCao> tuCaoList) {
        this.tuCaoList = tuCaoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }

        ImageView ivTuCaoImg;
        ImageView ivSex;
        TextView tvContent;
        TextView tvPostMan;
        TextView tvCommentCount;
        TextView tvViewCount;
        TextView tvDate;
        LinearLayout llParent;
    }

    @Override
    public int getItemCount() {
        return tuCaoList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_tocao_list_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivTuCaoImg = (ImageView) view.findViewById(R.id.iv_tuCaoImg);
        viewHolder.ivSex = (ImageView) view.findViewById(R.id.iv_sex);
        viewHolder.llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_content);
        viewHolder.tvPostMan = (TextView) view.findViewById(R.id.tv_postMan);
        viewHolder.tvCommentCount = (TextView) view.findViewById(R.id.tv_commentCount);
        viewHolder.tvViewCount = (TextView) view.findViewById(R.id.tv_viewCount);
        viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_date);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        TuCao tuCao = tuCaoList.get(i);
        viewHolder.tvContent.setText(tuCao.getContent());
        // TODO: 2017/4/22 这里以后接入昵称 接入昵称 author已经改为nickname
        viewHolder.tvPostMan.setText(tuCao.getAuthor());
        viewHolder.tvCommentCount.setText(tuCao.getCommentCount());
        viewHolder.tvViewCount.setText(tuCao.getViewCount());
        viewHolder.tvDate.setText(tuCao.getDate().substring(5, 16));
        if (TextUtils.isEmpty(tuCao.getImgUrl())) {
            viewHolder.ivTuCaoImg.setVisibility(View.GONE);
        } else {
            viewHolder.ivTuCaoImg.setVisibility(View.VISIBLE);
            GlideUtil.setImageToView(tuCao.getImgUrl(), viewHolder.ivTuCaoImg);
        }
        if (tuCao.getSex().equals("1")) {
            viewHolder.ivSex.setImageResource(R.mipmap.ic_man);
        } else {
            viewHolder.ivSex.setImageResource(R.mipmap.ic_female);
        }
        if (mOnItemClickLitener != null) {
            viewHolder.llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.llParent, i);
                }
            });
        }
        if (mOnItemLongClickLitener != null) {
            viewHolder.llParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickLitener.onItemLongClick(viewHolder.llParent, i);
                    return true;
                }

            });
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickLitener {
        void onItemLongClick(View view, int position);
    }
    public OnItemClickLitener mOnItemClickLitener;
    public OnItemLongClickLitener mOnItemLongClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }
}
