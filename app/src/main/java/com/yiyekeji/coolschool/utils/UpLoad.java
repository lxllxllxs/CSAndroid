package com.yiyekeji.coolschool.utils;

import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/18.
 */
public class UpLoad {
    public static void  upLoadFile(File file){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        CommonService commonService = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = commonService.upLoad(requestFile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    LogUtil.d("upLoadSuccess");
                } else {
                    LogUtil.d("upLoadFail");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
