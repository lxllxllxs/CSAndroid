package com.yiyekeji.coolschool.inter;

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
public interface CommonService {

    /**
     *
     参数名称	参数类型	说明
     pType	String	手机型号
     pSize	String 	手机屏幕大小
     * @param params
     * @return
     */
    @POST("common/appUploadPhoneModel")
    Call<ResponseBody> appUploadPhoneModel(@Body Map<String, Object> params);

    @Multipart
    @POST("common/upLoad")
    Call<ResponseBody> upload( @Part MultipartBody.Part... file);

    @POST("common/checkVersion")
    Call<ResponseBody> checkVersion();

}
