package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.CreateDeliverExpressOrder;
import com.yiyekeji.coolschool.bean.CreateTakeExpressOrder;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/2/7.
 */
public interface ExpressService {

    //用户获取其订单
    @POST("express/getUserOtherOrderList ")
    Call<ResponseBody> getUserOtherOrderList(@Body  Map<String, Object> params);


    @POST("express/createDoorExpressOrder ")
    Call<ResponseBody> createDoorExpressOrder(@Body CreateTakeExpressOrder order);

    /**
     * 与我的命名冲突
     * @param order
     * @return
     */
    @POST("express/createTakeExpressOrder ")
    Call<ResponseBody> createDeliverExpressOrder(@Body CreateDeliverExpressOrder order);

    @POST("express/getOtherOrderList")
    Call<ResponseBody> getOtherOrderList(@Body Map<String, Object> params);

    @POST("express/updateOtherOrderState")
    Call<ResponseBody> updateOtherOrderState(@Body Map<String, Object> params);

}
