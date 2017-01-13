package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.StudentSign;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/9.
 */
public class StudentSignInActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initData();
    }

    private void initData() {
        getMyCoures();
    }

    RollCallService  service;
    List<CourseInfo> infos;
    private void getMyCoures() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());

        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.getMyCourse(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                infos= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseInfo>>() {}.getType(),"courseInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos!=null) {
                    for (CourseInfo info:infos){
                        LogUtil.d(info.toString());
                    }
                } else {
                    showShortToast(rb.getMessage());
                }

                startSignIn();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });
    }


    private void  startSignIn(){
/*        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());

        params.put("ip", App.userInfo.getUserNum());
        params.put("IMEI", App.userInfo.getUserNum());
        params.put("courseNo", App.userInfo.getUserNum());*/
        List<String> courseNos = new ArrayList<>();
        for (CourseInfo info:infos){
            courseNos.add(info.getCourseNo());
        }
        StudentSign signIn = new StudentSign();

        signIn.setIp("192.168.10.187");
        signIn.setCourseNo(courseNos);
        signIn.setIMEI("123456789");
        signIn.setTokenId(App.userInfo.getTokenId());
        signIn.setUserNum(App.userInfo.getUserNum());

        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.studentSignIn(signIn);
        showLoadDialog("");
        Gson gson = new Gson();
        LogUtil.d("StudentSign"+gson.toJson(signIn));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("签到成功！");
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });

    }
}
