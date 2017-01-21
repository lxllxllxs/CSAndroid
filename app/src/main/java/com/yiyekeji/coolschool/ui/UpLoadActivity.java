package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/18.
 */
public class UpLoadActivity extends BaseActivity {
    CommonService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RetrofitUtil.create(CommonService.class);
        upLoad2();
    }
/*
    private void upLoad(){
        File file = new File("/sdcard/2.docx");//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        Call<ResponseBody> call = service.upload("img", requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                System.out.println("success"+response.code());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void upLoad2(){
        File file = new File("/sdcard/4.jpg");//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        3、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        //第二个参数对应服务器的 getOriginalFilename
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileUpload", file.getName(), requestFile);
        MultipartBody.Part part4 = MultipartBody.Part.createFormData("fileUpload2", file.getName()+"copy", requestFile);
//        3、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        //第二个参数对应服务器的 getOriginalFilename
        MultipartBody.Part part2 = MultipartBody.Part.createFormData("userNum", App.userInfo.getUserNum());
        MultipartBody.Part part3 = MultipartBody.Part.createFormData("type", "0");

        Call<ResponseBody> call = service.upload(part,part2,part3,part4);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("上传成功！");
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
}
