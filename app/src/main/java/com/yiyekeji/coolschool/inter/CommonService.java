package com.yiyekeji.coolschool.inter;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}
