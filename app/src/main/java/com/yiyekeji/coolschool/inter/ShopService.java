package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.ReleaseProduct;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lxl on 2017/1/19.
 */
public interface ShopService {
    @POST("shop/getShopCategoryList")
    Call<ResponseBody> getShopCategoryList();


    @POST("shop/getShopAdvertList")
    Call<ResponseBody> getShopAdvertList();

    @POST("shop/publicProduct")
    Call<ResponseBody> publicProduct(@Body ReleaseProduct releaseProduct);

    @POST("shop/getProductLIst")
    Call<ResponseBody> getProductLIst(@Body Map<String, Object> params);
}
