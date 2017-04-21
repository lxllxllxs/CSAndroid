package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductImage;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<ProductImage> imgList;
    private Context context;

    public SelectImageAdapter(Context context, List<ProductImage> imgList) {
        this.imgList = imgList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void notifyDataSetChanged(List<ProductImage> imgList) {
        this.imgList = imgList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        FrameLayout flContainer;
        ImageView ivAdd;
        ImageView ivDel;
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_add_image_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.flContainer=(FrameLayout)view.findViewById(R.id.fl_container);
        viewHolder.ivAdd = (ImageView) view.findViewById(R.id.iv_add);
        viewHolder.ivDel = (ImageView) view.findViewById(R.id.iv_del);

        viewHolder.ivDel.setVisibility(View.GONE);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        ProductImage image=imgList.get(i);
        if (!isDelVisiable) {
            viewHolder.ivDel.setVisibility(View.GONE);
        } else {
            viewHolder.ivDel.setVisibility(View.VISIBLE);
        }
        GlideUtil.setImageToView(image.getImgUrl(),viewHolder.ivAdd);
        if (onItemClickLitener2 != null) {
            viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitener2.onItemClick(v,i);
                }
            });
        }
        if (mOnItemClickLitener != null) {
            viewHolder.flContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v,i);
                }
            });
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    boolean isDelVisiable = false;
    public void setDelVisiable(boolean b){
        isDelVisiable = b;
        notifyDataSetChanged();
    }

    public OnItemClickLitener mOnItemClickLitener,onItemClickLitener2;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setDelOnClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.onItemClickLitener2 = mOnItemClickLitener;
    }

}
