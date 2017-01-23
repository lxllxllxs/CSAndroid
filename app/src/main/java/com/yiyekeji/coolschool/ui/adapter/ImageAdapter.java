package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> imgPathList;
    private Context context;

    public ImageAdapter(Context context, List<String> imgPathList) {
        this.imgPathList = imgPathList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<String> imgPathList) {
        this.imgPathList = imgPathList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        ImageView imageView;
    }

    @Override
    public int getItemCount() {
        return imgPathList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_only_square_image, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
        viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        GlideUtil.setImageToView(imgPathList.get(i),viewHolder.imageView);
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
