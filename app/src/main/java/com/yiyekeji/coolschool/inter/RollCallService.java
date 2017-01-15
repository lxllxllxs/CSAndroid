package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.StudentSign;

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

    @POST("callname/startCallName")
    Call<ResponseBody> startCallName(@Body Map<String, Object> params);



    @POST("callname/getMyCourse")
    Call<ResponseBody> getMyCourse(@Body Map<String, Object> params);



    @POST("callname/studentSignIn")
    Call<ResponseBody> studentSignIn(@Body StudentSign studentSign);

    @POST("callname/getNewSignsNum")
    Call<ResponseBody> getNewSignsNum(@Body Map<String, Object> params);

    @POST("callname/getSignsInfo  ")
    Call<ResponseBody> getSignsInfo  (@Body Map<String, Object> params);
}
