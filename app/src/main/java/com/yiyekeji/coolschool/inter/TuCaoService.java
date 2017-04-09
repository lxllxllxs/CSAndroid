package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.bean.TuCaoComment;

import java.util.Map;

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
    @POST("tucao/upLoadTuCao")
    Call<ResponseBody> upLoadTuCao( @Part MultipartBody.Part... file);

    @POST("tucao/getTuCaoList")
    Call<ResponseBody> getTuCaoList(@Body Map<String, Object> params);

    @POST("tucao/commentTuCao")
    Call<ResponseBody> commentTuCao(@Body TuCaoComment comment);

    @POST("tucao/getCommentList")
    Call<ResponseBody> getCommentList(@Body Map<String, Object> params);

    @POST("tucao/updateViewCount")
    Call<ResponseBody> updateViewCount(@Body Map<String, Object> params);



    @POST("tucao/delTuCao")
    Call<ResponseBody> delTuCao(@Body Map<String, Object> params);
}
