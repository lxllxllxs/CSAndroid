package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CategoryInfo;
import com.yiyekeji.coolschool.bean.ProductImage;
import com.yiyekeji.coolschool.bean.ProductInfo;
import com.yiyekeji.coolschool.bean.ProductModel;
import com.yiyekeji.coolschool.bean.ReleaseProduct;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.AddImageAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.LableEditView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/19.
 */
public class EditProductAyt extends BaseActivity {
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
    ArrayList<Integer> imgsId = new ArrayList<>(5);

    List<String> imgPathList = new ArrayList<>(5);
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
        setContentView(R.layout.activity_edit_product);
        ButterKnife.inject(this);
        titleBar.initView(this);
        initData();
    }

    private void initData() {
        initImageIds();
        service = RetrofitUtil.create(CommonService.class);
        int pId=getIntent().getIntExtra("pId",0);
        getProductInfo(pId);
    }


    //// TODO: 2017/4/22 提交后需要重新设置
    private void initImageIds() {
        imgsId.clear();
        imgPathList.clear();
        for (int i=0;i<5;i++){
            imgPathList.add("");
            imgsId.add(0);
        }
    }

    AddImageAdapter imageAdapter;
    private void initViewAfter() {
        ledtUtil.setEditText(productInfo.getpUnit());
        ledtTitle.setEditText(productInfo.getpTitle());
        edtDescrition.setText(productInfo.getpDescrition());
//        imgPathList=productInfo.getPictureList();

        imageAdapter = new AddImageAdapter(this,imgPathList,1);
        rvImgs.setLayoutManager(new GridLayoutManager(this,3));
        rvImgs.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickLitener(new AddImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO: 2017/4/21/021 记录位置
                currentIndex = position;
                Intent intent = new Intent(EditProductAyt.this,SelectProductImageAty.class);
                startActivityForResult(intent, CHOOSE_IMAGE);
            }
        });

        imageAdapter.setDelOnClickListener(new AddImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                imgPathList.set(position,"");
                imgsId.set(position,0);
                imageAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setReleaseProduct() {
        releaseProduct.setSupplierNum(App.userInfo.getUserNum());
        List<Integer> tempList = new ArrayList<>();
        for (int i:imgsId){
            if (i==0){
                tempList.add(i);
            }
        }
        imgsId.removeAll(tempList);
        // TODO: 2017/4/21/021 注意这里要传入产品的id
        releaseProduct.setpId(productInfo.getpId());
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
                    if (imgPathList.contains("")) {
                        imgPathList.remove("");
                    }
                    if (imgPathList.isEmpty()){
                        return;
                    }
//                    upLoadImage(imgPathList.get(0));
                    setReleaseProduct();
                    releaseProduct();
                }
                break;
        }
    }

    private ProductInfo productInfo=new ProductInfo();
    private void getProductInfo(int id) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("pId", id);
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.getProductInfo(parms);
        showLoadDialog("");
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
                    productInfo = GsonUtil.fromJSon(GsonUtil.getValueByTag(jsonString, "productInfo"),
                            ProductInfo.class);
                    if (productInfo == null) {
                        return;
                    }
                    // TODO: 2017/4/21/021 获得原先的
                    releaseProduct.setModelList(productInfo.getModelList());
                    initViewAfter();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(getString(R.string.response_err));
            }
        });
    }


    private boolean check() {
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
        Call<ResponseBody> call = service.editProduct(releaseProduct);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    initImageIds();
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("修改成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    initImageIds();
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                initImageIds();
            }
        });
    }


    private int currentIndex=0;
    private void showImg(Intent intent) {
        ProductImage selectedImage = intent.getParcelableExtra("image");
     /*   switch (currentIndex) {
            case 0:

                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;

        }*/
        // TODO: 2017/4/21/021 图片显示和实际上传的图片id分开  sortId暂未使用
        imgPathList.set(currentIndex,selectedImage.getImgUrl());
        imgsId.set(currentIndex, selectedImage.getId());
        imageAdapter.notifyDataSetChanged();

    }

}
