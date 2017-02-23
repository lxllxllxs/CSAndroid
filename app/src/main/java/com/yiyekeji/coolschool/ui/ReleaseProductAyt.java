package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.Config;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CategoryInfo;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.bean.ReleaseProduct;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.AddImageAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GetPathFromUri4kitkat;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RegexUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import net.bither.util.NativeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    @InjectView(R.id.rv_imgs)
    RecyclerView rvImgs;


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

        imgPathList.add(R.mipmap.ic_add_pic+"");
        releaseProduct.setModelList(new ArrayList<ProductModel>());
    }

    AddImageAdapter imageAdapter;
    private void initView() {
        titleBar.initView(this);
        imageAdapter = new AddImageAdapter(this, imgPathList);
        rvImgs.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rvImgs.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickLitener(new AddImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                selectImg();
            }
        });

        imageAdapter.setDelOnClickListener(new AddImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (RegexUtils.checkDigit(imgPathList.get(position))||imgPathList.size()==1) {
                    imgPathList.set(position,R.mipmap.ic_add_pic+"");
                    imageAdapter.notifyDataSetChanged();
                    return;
                }
                imgPathList.remove(position);
                imageAdapter.notifyDataSetChanged();
            }
        });
    }


    private void upLoadImage(final String filePath) {
        File file = new File(filePath);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        Bitmap bitmap= BitmapFactory.decodeFile(filePath);
        if (!new File(Config.IMG_TEMP_PATH).exists()) {
            new File(Config.IMG_TEMP_PATH).mkdirs();
        }
         NativeUtil.compressBitmap(bitmap,Config.IMG_TEMP_PATH+System.currentTimeMillis()+".jpg",true);
      /*  if (true) {
            return;
        }*/
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
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
                t.printStackTrace();
            }
        });
    }

    private void setReleaseProduct() {
        releaseProduct.setSupplierNum(App.userInfo.getUserNum());
        Collections.sort(imgsId);
        releaseProduct.setPictureIdList(imgsId);
    }

    @OnClick({ R.id.ll_category, R.id.ll_model, R.id.tv_cancel, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
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
                //先检查 有无空参数(除了默认的添加图片)
                if (check()) {
                    if (imgPathList.contains(R.mipmap.ic_add_pic+"")) {
                        imgPathList.remove(R.mipmap.ic_add_pic + "");
                    }
                    if (imgPathList.isEmpty()){
                        return;
                    }
                    upLoadImage(imgPathList.get(0));
                }
                break;
        }
    }

    private boolean check() {
        if (imgPathList.size()==1&&RegexUtils.checkDigit(imgPathList.get(0))){
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

        releaseProduct.setTokenId(App.geTokenId());
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
                    finish();
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
        //
        if (imgPathList.size() >= 5) {
            imgPathList.remove(0);
        }
        imgPathList.add(pic_path);
        imageAdapter.notifyDataSetChanged();

    }

}
