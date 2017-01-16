package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
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
import com.yiyekeji.coolschool.widget.LoadDialog;

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
     /*   for (int i=100;i<250;i++){
            testLogin("3112000"+i,"qqqqqq");
        }*/
        getMyCoures();
    }

    UserService userService;
    private void testLogin(String name,String pwd) {
        final UserInfo user = new UserInfo();
        user.setUserNum(name);
        user.setPassword(pwd);

        user.setImei(CommonUtils.getIMEI());
        userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call= userService.login(user);
//        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                getLoadDialog().dismiss();
                if (response.code()!=200){
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                UserInfo  userInfo= GsonUtil.fromJSon(jsonString,UserInfo.class,"userInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (userInfo!=null) {
                    testGetMyCoures(userInfo.getTokenId(), userInfo.getUserNum());
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

    private void testGetMyCoures(final String tokenId, final String userNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", tokenId);
        params.put("userNum", userNum);
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.getMyCourse(params);
//        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = GsonUtil.toJsonString(response);
                List<CourseInfo>   infos= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseInfo>>() {}.getType(),"courseInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos!=null) {
                    testStartSignIn(infos, tokenId, userNum);
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
                    startSignIn();
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

    private int count=0;
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
                    LogUtil.d("testSignIn:Success=="+userNum);
//                    showResultDialog();
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



    private void  startSignIn(){
        List<String> courseNos = new ArrayList<>();
        for (CourseInfo info:infos){
            courseNos.add(info.getCourseNo());
        }
        StudentSign signIn = new StudentSign();

        signIn.setIp(NetUtils.getIpAddress());
        signIn.setCourseNo(courseNos);
        signIn.setImei(CommonUtils.getIMEI());
        signIn.setTokenId(App.userInfo.getTokenId());
        signIn.setUserNum(App.userInfo.getUserNum());

        //放亿业科技坐标测试
        signIn.setX(113.02954);
        signIn.setY(22.622995);
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.studentSignIn(signIn);
        showLoadDialog("正在签到");
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
                    showResultDialog();
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


    private void showResultDialog(){
        LoadDialog mdDialog = new LoadDialog(this, R.layout.layout_image,
                R.style.DialogNoBg);
        mdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        mdDialog.show();
    }
}
