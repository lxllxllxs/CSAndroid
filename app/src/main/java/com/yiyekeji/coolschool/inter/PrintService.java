package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.CreatePrintOrder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/2/6.
 */
public interface PrintService {
    @POST("print/createPrintOrder")
    Call<ResponseBody> createPrintOrder(@Body CreatePrintOrder order);
}
