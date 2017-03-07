package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.TuCao;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.InjectView;

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
        @InjectView(R.id.tv_content)
        TextView tvContent;
        @InjectView(R.id.tv_postMan)
        TextView tvPostMan;
        @InjectView(R.id.tv_commentCount)
        TextView tvCommentCount;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.ll_parent)
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
        viewHolder.llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_content);
        viewHolder.tvPostMan = (TextView) view.findViewById(R.id.tv_postMan);
        viewHolder.tvCommentCount = (TextView) view.findViewById(R.id.tv_commentCount);
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
        viewHolder.tvPostMan.setText(tuCao.getPostMan());
        viewHolder.tvCommentCount.setText(tuCao.getCommentCount());
        viewHolder.tvDate.setText(tuCao.getDate());

        if (mOnItemClickLitener != null) {
            viewHolder.llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.llParent, i);
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
