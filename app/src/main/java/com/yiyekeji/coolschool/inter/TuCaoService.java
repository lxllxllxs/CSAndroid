package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.TuCao;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lxl on 2017/1/5.
 */
public interface TuCaoService {

    @POST("tucao/insertTuCao")
    Call<ResponseBody> insertTuCao(@Body TuCao tuCao);

    @Multipart
    @POST("tucao/upLoadImg")
    Call<ResponseBody> upLoadImg( @Part MultipartBody.Part... file);


}
