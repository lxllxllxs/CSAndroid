package com.yiyekeji.coolschool.inter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/1/19.
 */
public interface ShopService {
    @POST("shop/getShopCategory")
    Call<ResponseBody> getShopCategory();

    @POST("shop/getShopAdvert")
    Call<ResponseBody> getShopAdvert();

}
