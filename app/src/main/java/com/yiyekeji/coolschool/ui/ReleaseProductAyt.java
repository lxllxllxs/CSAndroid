package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GetPathFromUri4kitkat;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/19.
 */
public class ReleaseProductAyt extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.iv_add)
    ImageView ivAdd;
    @InjectView(R.id.iv_2)
    ImageView iv2;
    @InjectView(R.id.iv_3)
    ImageView iv3;
    @InjectView(R.id.iv_4)
    ImageView iv4;
    @InjectView(R.id.iv_5)
    ImageView iv5;
    @InjectView(R.id.ll_parent)
    LinearLayout llParent;
    @InjectView(R.id.ledt_title)
    LableEditView ledtTitle;
    @InjectView(R.id.ll_category)
    LinearLayout llCategory;
    @InjectView(R.id.ll_price)
    LinearLayout llPrice;
    @InjectView(R.id.ledt_util)
    LableEditView ledtUtil;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_product);
        ButterKnife.inject(this);
        initView();
    }

    List<ImageView> imageViews = new ArrayList<>();
    private void initView() {
        titleBar.initView(this);
        imageViews.add(iv2);
        imageViews.add(iv3);
        imageViews.add(iv4);
        imageViews.add(iv5);
        imageViews.add(ivAdd);
    }

    @OnClick({R.id.iv_add, R.id.ll_category, R.id.ll_price, R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                selectImg();
                break;
            case R.id.ll_category:
                break;
            case R.id.ll_price:
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                break;
        }
    }

    final int CHOOSE_IMAGE=0x123;
    private void selectImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CHOOSE_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode) {
            //处理图库返回
            case CHOOSE_IMAGE:
                showImg(data);
                break;
         /*   //处理相机返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    File file = new File(filePath);
                    photoZoom(Uri.fromFile(file));
                }
                //处理裁剪返回
            case PHOTO_RESULT:
                Bundle bundle = new Bundle();
                try {
                    bundle = data.getExtras();
                    if (resultCode == RESULT_OK) {
                        Bitmap bitmap = bundle.getParcelable("data");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, new ByteArrayOutputStream());
                        //修改ImageView的图片
                        photoImage.setImageBitmap(bitmap);
                    }
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }
                break;*/
        }
    }

    String pic_path;
    List<String> imgPathList = new ArrayList<>();
    private void showImg(Intent data) {
        Uri selectedImage = data.getData();
        pic_path = GetPathFromUri4kitkat.getPath(this, selectedImage);
        if (TextUtils.isEmpty(pic_path)) {
            return;
        }
        //判断是否重复值
        if (imgPathList.contains(pic_path)) {
            return;
        }
        imgPathList.add(pic_path);
        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pic_path, options);
        int scale = (int) (options.outWidth / (float) 300);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pic_path, options);
        imageViews.get(imgPathList.size()-1).setImageBitmap(bitmap);
        imageViews.get(imgPathList.size()-1).setMaxHeight(140);
        imageViews.get(imgPathList.size()-1).setVisibility(ImageView.VISIBLE);
    }

}
