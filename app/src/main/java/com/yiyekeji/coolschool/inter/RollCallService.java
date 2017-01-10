package com.yiyekeji.coolschool.inter;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/1/10.
 */
public interface RollCallService {


    @POST("callname/getAllCourse")
    Call<ResponseBody> getAllCourse(@Body Map<String, Object> params);

}
