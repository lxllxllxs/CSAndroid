package com.yiyekeji.coolschool.ui.test;

import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.StudentSign;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.NetUtils;
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
 * Created by lxl on 2017/2/22.
 */
public class TestSign extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testStudentSign();
    }
    UserService userService;
    RollCallService service;

    private int count;
    public void testStudentSign(){
        for (int i=545;i<1500;i++){
            final UserInfo user = new UserInfo();
            user.setUserNum("0000000"+i);
            user.setPassword("qqqqqq");
            user.setImei(CommonUtils.getIMEI());
            userService = RetrofitUtil.create(UserService.class);
            Call<ResponseBody> call= userService.login(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code()!=200){
                        return;
                    }
                    String jsonString = GsonUtil.toJsonString(response);
                    UserInfo  userInfo= GsonUtil.fromJSon(jsonString,UserInfo.class,"userInfo") ;
                    ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                    if (rb.getResult().equals("1")){
                        count++;
                        LogUtil.d("第"+count+"个登录成功："+user.getUserNum());
                    }
                    if (userInfo!=null) {
                        testGetMyCoures(userInfo.getTokenId(), userInfo.getUserNum());
                    } else {
                        LogUtil.d(rb.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LogUtil.d(t.toString());
                }
            });
        }
    }


    private void  testStartSignIn(List<CourseInfo> infos, String tokenId, final String userNum){
        List<String> courseNos = new ArrayList<>();
        count++;
        for (CourseInfo info:infos){
            courseNos.add(info.getCourseNo());
        }
        StudentSign signIn = new StudentSign();

        signIn.setIp(NetUtils.getIpAddress()+count);
        signIn.setCourseNo(courseNos);
        signIn.setImei(CommonUtils.getIMEI()+count);
        signIn.setTokenId(tokenId);
        signIn.setUserNum(userNum);

        //放亿业科技坐标测试
        signIn.setX(113.02954);
        signIn.setY(22.622995);
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.studentSignIn(signIn);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()!=200){
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    LogUtil.d("签到成功:Success=="+userNum+"=="+count);
//                    showResultDialog();
                } else {
                    LogUtil.d(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.d(t.toString());
            }
        });
    }

    private void testGetMyCoures(final String tokenId, final String userNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", tokenId);
        params.put("userNum", userNum);
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.getMyCourse(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = GsonUtil.toJsonString(response);
                List<CourseInfo> infos= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseInfo>>() {}.getType(),"courseInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos!=null) {
                    testStartSignIn(infos, tokenId, userNum);
                } else {
                    LogUtil.d(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.d(t.toString());
            }
        });
    }
}
