package com.yiyekeji.coolschool.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.yiyekeji.coolschool.bean.AdvertInfo;
import com.yiyekeji.coolschool.utils.GlideUtil;

import java.util.List;

/**
 * Created by lxl on 2016/7/27.
 */
public class RollPagerViewAdapter extends StaticPagerAdapter {
    private boolean isCrop=true;
    private List<AdvertInfo> AdvertInfoList;
    public RollPagerViewAdapter(List<AdvertInfo> AdvertInfoList){
        this.AdvertInfoList = AdvertInfoList;
    }

    public RollPagerViewAdapter(List<AdvertInfo> AdvertInfoList, boolean isCrop){
        this.isCrop=false;
        this.AdvertInfoList = AdvertInfoList;
    }
    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());

        if (mOnItemClickLitener!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view,position);
                }
            });
        }
        GlideUtil.resizeToView(AdvertInfoList.get(position).getAdvertImage(), view,
                768, 200);
        view.setScaleType(ImageView.ScaleType.FIT_START);
        GlideUtil.setImageToView(AdvertInfoList.get(position).getAdvertImage(),view);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return AdvertInfoList.size();
    }

    public void notifyDataSetChanged(List<AdvertInfo> adList) {
        this.AdvertInfoList = adList;
        notifyDataSetChanged();
    }


    /**
     * 自定义一个回调点击函数
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    /**
     * 点击事件设置回调
     * @param mOnItemClickLitener
     */
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}