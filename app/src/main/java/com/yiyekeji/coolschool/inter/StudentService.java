package com.yiyekeji.coolschool.inter;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/3/29.
 */
public interface StudentService {
    @POST("student/getPullMsg")
    Call<ResponseBody> getPullMsg (@Body Map<String, Object> params);
}
