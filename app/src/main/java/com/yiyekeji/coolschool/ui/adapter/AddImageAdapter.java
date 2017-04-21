package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lxl on 2016/10/25.
 */
public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<String> imgPathList;
    private Context context;
    private int type=0;

    public AddImageAdapter(Context context, List<String> imgPathList) {
        this.imgPathList = imgPathList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public AddImageAdapter(Context context, List<String> imgPathList,int type) {
        this.imgPathList = imgPathList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.type=type;
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
        FrameLayout flContainer;
        ImageView ivAdd;
        ImageView ivDel;
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
        View view;
        switch (type){
            case 0:
                view = mInflater.inflate(R.layout.item_add_image_adapter, viewGroup, false);
                break;
            case 1:
                view = mInflater.inflate(R.layout.item_add_image_240_adapter, viewGroup, false);
                break;
            default:
                view = mInflater.inflate(R.layout.item_add_image_adapter, viewGroup, false);
                break;
        }
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.flContainer=(FrameLayout)view.findViewById(R.id.fl_container);
        viewHolder.ivAdd = (ImageView) view.findViewById(R.id.iv_add);
        viewHolder.ivDel = (ImageView) view.findViewById(R.id.iv_del);
        return viewHolder;
    }

    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        String path=imgPathList.get(i);
        //如果是数字 即为resId

        if (RegexUtils.checkDigit(path)){
            viewHolder.ivAdd.setImageResource(Integer.valueOf(path));
            viewHolder.ivDel.setVisibility(View.GONE);
            //处理网址
        } else if (path.contains("http://")) {
            viewHolder.ivDel.setVisibility(View.VISIBLE);
            GlideUtil.setImageToView(path,viewHolder.ivAdd);
        }else if (TextUtils.isEmpty(path)) {
            viewHolder.ivDel.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ivDel.setVisibility(View.VISIBLE);
            viewHolder.ivAdd.setImageBitmap(zoomImage(i));
        }
        viewHolder.ivAdd.setMaxHeight(140);
        if (mOnItemClickLitener != null) {
            viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v,i);
                }
            });
        }
        if (onItemClickLitener2 != null) {
            viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitener2.onItemClick(v,i);
                }
            });
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    /**
     *
     * @param i
     * @return
     */
    private Bitmap zoomImage(int i){
        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPathList.get(i), options);
        int scale = (int) (options.outWidth / (float) 140);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(imgPathList.get(i), options);
    }

    public OnItemClickLitener mOnItemClickLitener,onItemClickLitener2;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setDelOnClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.onItemClickLitener2 = mOnItemClickLitener;
    }

}
