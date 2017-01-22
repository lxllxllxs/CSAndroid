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
import com.yiyekeji.coolschool.bean.CategoryInfo;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.bean.ReleaseProduct;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GetPathFromUri4kitkat;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.LableEditView;
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
    @InjectView(R.id.ll_model)
    LinearLayout llPrice;
    @InjectView(R.id.ledt_util)
    LableEditView ledtUtil;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    CommonService service;
    List<ImageView> imageViews = new ArrayList<>();
    ArrayList<Integer> imgsId = new ArrayList<>();

    String pic_path;
    List<String> imgPathList = new ArrayList<>();
    ReleaseProduct releaseProduct = new ReleaseProduct();
    final int CHOOSE_IMAGE = 0x123;
    final int SELECT_CATEGORY = 0x122;
    final int ADD_MODEL = 0x121;
    @InjectView(R.id.edt_descrition)
    EditText edtDescrition;
    @InjectView(R.id.tv_category)
    TextView tvCategory;
    @InjectView(R.id.iv_d1)
    ImageView ivD1;
    @InjectView(R.id.iv_d2)
    ImageView ivD2;
    @InjectView(R.id.iv_d3)
    ImageView ivD3;
    @InjectView(R.id.iv_d4)
    ImageView ivD4;
    @InjectView(R.id.iv_d5)
    ImageView ivD5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_product);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        service = RetrofitUtil.create(CommonService.class);

        releaseProduct.setModelList(new ArrayList<ProductModel>());
    }

    private void initView() {
        titleBar.initView(this);
        imageViews.add(iv2);
        imageViews.add(iv3);
        imageViews.add(iv4);
        imageViews.add(iv5);
        imageViews.add(ivAdd);

        ivD1.setOnClickListener(delCk);
        ivD2.setOnClickListener(delCk);
        ivD3.setOnClickListener(delCk);
        ivD4.setOnClickListener(delCk);
        ivD5.setOnClickListener(delCk);
    }

    private View.OnClickListener delCk =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_d1:
                    imgPathList.remove(imgPathList.size()-1);
                    break;
                case R.id.iv_d2:
                    imgPathList.remove(0);
                    break;
                case R.id.iv_d3:
                    imgPathList.remove(1);
                    break;
                case R.id.iv_d4:
                    imgPathList.remove(2);
                    break;
                case R.id.iv_d5:
                    imgPathList.remove(3);
                    break;
            }
            if (v.getId() == R.id.iv_d1) {
                ivAdd.setImageResource(R.mipmap.ic_add_pic);
                v.setVisibility(View.GONE);
                return;
            }
            ((View)v.getParent()).setVisibility(View.GONE);
        }
    };
    private void upLoadImage(final String filePath) {
        File file = new File(filePath);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileUpload", file.getName(), requestFile);
        MultipartBody.Part part2 = MultipartBody.Part.createFormData("userNum", App.userInfo.getUserNum());
        MultipartBody.Part part3 = MultipartBody.Part.createFormData("type", "0");
        showLoadDialog("");
        Call<ResponseBody> call = service.upload(part, part2, part3);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
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
                    if (ids != null) {
                        imgsId.addAll(ids);
                        LogUtil.d("上传成功！");
                    } else {
                        LogUtil.d("上传失败！");
                    }
                    if (imgPathList.size() > 0) {
                        upLoadImage(imgPathList.get(0));
                        return;
                    }
                    setReleaseProduct();
                    releaseProduct();
                    LogUtil.d("总共上传文件大小：" + imgsId.size() + "");
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setReleaseProduct() {
        releaseProduct.setSupplierNum(App.userInfo.getUserNum());
        releaseProduct.setPictureIdList(imgsId);
    }

    @OnClick({R.id.iv_add, R.id.ll_category, R.id.ll_model, R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                selectImg();
                break;
            case R.id.ll_category: {
                Intent intent = new Intent(this, SelectCategoryAty.class);
                startActivityForResult(intent, SELECT_CATEGORY);
            }
            break;
            case R.id.ll_model: {
                Intent intent = new Intent(this, ModifyProductModelAty.class);
                intent.putExtra("modelList", releaseProduct.getModelList());
                startActivityForResult(intent, ADD_MODEL);
            }
            break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                //先检查 有无空参数
                if (check()) {
                    upLoadImage(imgPathList.get(0));
                }
                break;
        }
    }

    private boolean check() {
        if (imgPathList.isEmpty()) {
            showShortToast("图片不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(ledtTitle.getEditText())) {
            showShortToast("标题不能为空！");
            return false;
        }
        if (ledtTitle.getEditText().length() > 30) {
            showShortToast("标题不能超过30个字！");
            return false;
        }
        releaseProduct.setpTitle(ledtTitle.getEditText());
        if (releaseProduct.getCategoryId() == 0) {
            showShortToast("还没选择分类！");
            return false;
        }
        if (releaseProduct.getModelList() == null) {
            showShortToast("还没设置规格！");
            return false;
        }
        if (TextUtils.isEmpty(ledtUtil.getEditText())) {
            showShortToast("计量单位不能为空！");
            return false;
        }
        releaseProduct.setpUnit(ledtUtil.getEditText());
        if (TextUtils.isEmpty(edtDescrition.getText())) {
            showShortToast("详情不能为空！");
            return false;
        }
        releaseProduct.setpDescrition(edtDescrition.getText().toString());
        return true;
    }

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
            case SELECT_CATEGORY:
                CategoryInfo info = data.getParcelableExtra("category");
                if (info == null) {
                    return;
                }
                releaseProduct.setCategoryId(info.getCategoryId());
                tvCategory.setText(info.getCategoryName());
                break;
            case ADD_MODEL:
                ArrayList<ProductModel> modelList = data.getParcelableArrayListExtra("modelList");
                if (modelList == null) {
                    return;
                }
                releaseProduct.setModelList(modelList);
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

    private void releaseProduct() {
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.publicProduct(releaseProduct);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("发布成功！");
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
            }
        });
    }

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
        //判断是否超过5张 为真移出最初一张
        if (imgPathList.size()>=5) {
            imgPathList.remove(0);
        }
        imgPathList.add(pic_path);


    }

}
