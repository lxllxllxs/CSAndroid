package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ProductImage;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.ShopService;
import com.yiyekeji.coolschool.ui.adapter.SelectImageAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.FileUtils;
import com.yiyekeji.coolschool.utils.GetPathFromUri4kitkat;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;
import com.yiyekeji.coolschool.widget.PullToRefreshRecycleView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/21.
 */
public class SelectProductImageAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    RecyclerView recyclerView;

    boolean isEdit = false;
    SelectImageAdapter mAdapter;

    static List<ProductImage> imageList = new ArrayList<>();
    ArrayList<Integer> imgsId = new ArrayList<>();

    final int CHOOSE_IMAGE = 0x123;
    @InjectView(R.id.prrv_pull_refresh_view)
    PullToRefreshRecycleView prrvPullRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_pulltorefresh_recycleview);
        ButterKnife.inject(this);
        initView();
        initData();
    }


    private void initView() {
        titleBar.initView(this);

        titleBar.setTvRight("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    isEdit = false;
                    mAdapter.setDelVisiable(false);
                    titleBar.setTvRightText("编辑");
                } else {
                    isEdit = true;
                    titleBar.setTvRightText("完成");
                    mAdapter.setDelVisiable(true);
                }

            }
        });

        titleBar.setTvRight2("上传", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });

        recyclerView=prrvPullRefreshView.getRefreshableView();
        prrvPullRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        prrvPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                refreshImageList();
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            }
        });

        mAdapter = new SelectImageAdapter(this, imageList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mAdapter.setOnItemClickLitener(new SelectImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductImage image = imageList.get(position);
                Intent intent = new Intent();
                intent.putExtra("image", image);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //删除
        mAdapter.setDelOnClickListener(new SelectImageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductImage image = imageList.get(position);
                showDialog(image);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void refreshImageList() {
        getProductImages();
    }

    private void initData() {
        if (imageList.isEmpty()) {
            getProductImages();
        }
    }

    private void selectImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    private void getProductImages() {
        ShopService service = RetrofitUtil.create(ShopService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("userNo", App.getUserInfo().getUserNum());
        Call<ResponseBody> call = service.getProductImages(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                prrvPullRefreshView.onRefreshComplete();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    List<ProductImage> tempList = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<ProductImage>>() {
                            }.getType(), "productImages");
                    if (tempList == null) {
                        return;
                    }
                    imageList.clear();
                    imageList.addAll(tempList);
                    mAdapter.notifyDataSetChanged(imageList);
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                prrvPullRefreshView.onRefreshComplete();
                dismissDialog();
            }
        });
    }

    private void delImage(final ProductImage image) {
        ShopService service = RetrofitUtil.create(ShopService.class);
        Call<ResponseBody> call = service.delImage(image);
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
                    //需要移除 被删的
                    imageList.remove(image);
                    mAdapter.notifyDataSetChanged();
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

    private void upLoadImage(final String filePath) {
        File file = new File(filePath);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileUpload", file.getName(), requestFile);
        MultipartBody.Part part2 = MultipartBody.Part.createFormData("userNum", App.userInfo.getUserNum());
        MultipartBody.Part part3 = MultipartBody.Part.createFormData("type", "0");
        showLoadDialog("");
        CommonService service = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = service.upload(part, part2, part3);
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
                    if (ids != null) {
                        showShortToast("上传成功！");
                    } else {
                        showShortToast("上传失败！");
                    }
                    LogUtil.d("总共上传文件大小：" + imgsId.size() + "");
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(getString(R.string.response_err));
                t.printStackTrace();
            }
        });
    }

    public void showDialog(final ProductImage image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除该图片吗？");//设置标题内容
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                delImage(image);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        final AlertDialog dlg = builder.create();
        dlg.show();
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
        }
    }

    String pic_path;

    private void showImg(Intent data) {
        Uri selectedImage = data.getData();
        pic_path = GetPathFromUri4kitkat.getPath(this, selectedImage);
        if (TextUtils.isEmpty(pic_path)) {
            return;
        }
        File file = new File(pic_path);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        try {
            if (FileUtils.getFileSize(file) > 1024) {
                showShortToast("图片不能大于1M");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        upLoadImage(pic_path);
    }

}
