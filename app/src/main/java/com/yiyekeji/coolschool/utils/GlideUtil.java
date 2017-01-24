package com.yiyekeji.coolschool.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;

/**
 * Created by lxl on 2016/11/09.
 */
public class GlideUtil {


    public static void setImageToView(String url, ImageView imageView) {

        if (TextUtils.isEmpty(url)||imageView==null) {
            return;
        }
        Glide.with(App.getContext())
                .load(Uri.parse(url))
                .placeholder(R.mipmap.loading_pic) //占位符 也就是加载中的图片，可放个gif
                .error(R.mipmap.load_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }



    //裁剪
    public static void resizeToView(String url, ImageView imageView,int width,int height) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.mipmap.load_fail);
        } else {
            //听说用uri比较快
            Glide.with(App.getContext())
                    .load(Uri.parse(url))
                    .override(width, height) // resizes the image to these dimensions (in pixel)
                    .fitCenter() // this croppin
                    .placeholder(R.mipmap.loading_pic)
                    .error(R.mipmap.load_fail)
                    .into(imageView);
        }
    }

}

