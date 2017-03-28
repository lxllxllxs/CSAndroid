package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.PullMsg;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/3/28.
 */
public interface TeacherService {
    @POST("teacher/inserPullMsg ")
    Call<ResponseBody> inserPullMsg (@Body PullMsg pullMsg);
}
