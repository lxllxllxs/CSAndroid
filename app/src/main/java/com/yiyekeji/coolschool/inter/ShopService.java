package com.yiyekeji.coolschool.inter;

import com.yiyekeji.coolschool.bean.CreateProductOrderInfo;
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

    @POST("shop/getProductList")
    Call<ResponseBody> getProductList(@Body Map<String, Object> params);

    @POST("shop/getProductInfo")
    Call<ResponseBody> getProductInfo(@Body Map<String, Object> params);

    @POST("shop/getSupplierInfo")
    Call<ResponseBody> getSupplierInfo(@Body Map<String, Object> params);

    @POST("shop/getSupplierProductList")
    Call<ResponseBody> getSupplierProductList(@Body Map<String, Object> params);

    @POST("shop/putUpOrOffProduct")
    Call<ResponseBody> putUpOrOffProduct(@Body Map<String, Object> params);

    @POST("shop/createProductOrder ")
    Call<ResponseBody> createProductOrder (@Body CreateProductOrderInfo createProductOrderInfo);

    @POST("shop/getTimeTypeList ")
    Call<ResponseBody> getTimeTypeList ();

    @POST("shop/addProductToCart ")
    Call<ResponseBody> addProductToCart (@Body Map<String, Object> params);

    @POST("shop/getSupplierProductOrderList ")
    Call<ResponseBody> getSupplierProductOrderList (@Body Map<String, Object> params);

    @POST("shop/getSupplierPorderInfo ")
    Call<ResponseBody> getSupplierPorderInfo (@Body Map<String, Object> params);

    @POST("shop/updateProductOrderState ")
    Call<ResponseBody> updateProductOrderState (@Body Map<String, Object> params);

    @POST("shop/getMyProductOrderList")
    Call<ResponseBody> getMyProductOrderList (@Body Map<String, Object> params);

}
