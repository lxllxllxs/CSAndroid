package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.inter.TuCaoService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.DateUtils;
import com.yiyekeji.coolschool.utils.FileUtils;
import com.yiyekeji.coolschool.utils.GetPathFromUri4kitkat;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/19.
 */
public class PublishTuCaoAyt extends BaseActivity {


    TuCaoService service;
    int imgsId;
    String pic_path;
    List<String> imgPathList = new ArrayList<>();
    TuCao tuCao = new TuCao();
    final int CHOOSE_IMAGE = 0x123;
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.iv_add)
    ImageView ivAdd;
    @InjectView(R.id.ll_img)
    LinearLayout llImg;
    @InjectView(R.id.edt_descrition)
    EditText edtDescrition;
    @InjectView(R.id.tv_addImg)
    TextView tvAddImg;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_tucao);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        service = RetrofitUtil.create(TuCaoService.class);
    }

    private void initView() {
        titleBar.initView(this);

    }


    private void upLoadImage(final String filePath) {
        // TODO: 2017/4/1 这里可能要添加大小限制
        File file = new File(filePath);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        try {
            if (FileUtils.getFileSize(file)>200) {
                showShortToast("图片不能大于200k");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    /*    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (!new File(Config.IMG_TEMP_PATH).exists()) {
            new File(Config.IMG_TEMP_PATH).mkdirs();
        }
        if (bitmap==null){
            showShortToast("压缩图错误");
            return;
        }
        try {
            NativeUtil.compressBitmap(bitmap, Config.IMG_TEMP_PATH + System.currentTimeMillis() + ".jpg", true);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileUpload", file.getName(), requestFile);
        MultipartBody.Part part2 = MultipartBody.Part.createFormData("userNum", App.userInfo.getUserNum());
        showLoadDialog("");
        Call<ResponseBody> call = service.upLoadTuCao(part, part2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    List<Integer> ids = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<Integer>>() {
                            }.getType(), "idS");
                    imgPathList.remove(filePath);//无论如何都不再重试
                    if (ids != null && ids.size() > 0) {
                        imgsId = ids.get(0);
                        LogUtil.d("上传成功！");
                    } else {
                        LogUtil.d("没有图片");
                    }
                    if (imgPathList.size() > 0) {
                        upLoadImage(imgPathList.get(0));
                        return;
                    }
                    publishTuCao();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(getString(R.string.response_err) + t.toString());
                t.printStackTrace();
            }
        });
    }

    private void setTuCao() {
        tuCao.setUserNo(App.userInfo.getUserNum());
        // FIXME: 2017/5/6 这里改为用昵称
        tuCao.setAuthor(App.userInfo.getNickname());
        tuCao.setSex(String.valueOf(App.userInfo.getSex()));
        tuCao.setDate(DateUtils.getTimeString());
        tuCao.setContent(edtDescrition.getText().toString());
        tuCao.setImgId(imgsId);
    }

    @OnClick({R.id.tv_confirm, R.id.tv_addImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addImg:
                selectImg();
                break;
            case R.id.tv_confirm:
                //先检查  没有图片则直接上传
                if (check()) {
                    if (imgPathList.isEmpty()) {
                        publishTuCao();
                        return;
                    }
                    upLoadImage(imgPathList.get(0));
                }
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(edtDescrition.getText())) {
            showShortToast("内容不能为空！");
            return false;
        }
        tuCao.setContent(edtDescrition.getText().toString());
        return true;
    }

    /**
     * 选择图片
     */
    private void selectImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            //处理图库返回
            case CHOOSE_IMAGE:
                showImg(data);
                break;
            //处理相机返回
        /*    case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    File file = new File(filePath);
                    photoZoom(Uri.fromFile(file));
                }
                break;
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

    /**
     *
     */
    private void publishTuCao() {
        setTuCao();
        TuCaoService service = RetrofitUtil.create(TuCaoService.class);
        Call<ResponseBody> call = service.insertTuCao(tuCao);
        showLoadDialog("正在发布...");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("发布成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
            }
        });
    }

    private void showImg(Intent data) {
        Uri selectedImage = data.getData();
        pic_path = GetPathFromUri4kitkat.getPath(this, selectedImage);
        if (TextUtils.isEmpty(pic_path)) {
            return;
        }
        File file = new File(pic_path);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        try {
            if (FileUtils.getFileSize(file)>200) {
                showShortToast("图片不能大于200k");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        tvAddImg.setText("换一张");
        llImg.setVisibility(View.VISIBLE);
        imgPathList.clear();
        imgPathList.add(pic_path);
        ivAdd.setImageURI(selectedImage);
    }

}
