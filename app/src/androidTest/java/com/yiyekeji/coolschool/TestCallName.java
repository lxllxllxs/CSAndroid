package com.yiyekeji.coolschool;

import android.test.InstrumentationTestCase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.StudentSign;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.NetUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestCallName extends InstrumentationTestCase {
    UserService userService;
    RollCallService  service;
/*    public void testTeacherCallName(){
        String[] teacherNum={"1111111111","2222222222","3333333333","4444444444","5555555555","6666666666"};
        for (String num : teacherNum) {
            login(num,"qqqqqq",true);
        }

    }*/

    String data="";

    private void con(UserInfo info) throws Exception {
        URL url = new URL("http://123.207.13.169:8080/cs/user/appLogin");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Gson gson = new Gson();
        byte[] datas =gson.toJson(info).getBytes();
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(15000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        urlConnection.setRequestProperty("Content-Length",String.valueOf(datas.length));
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        OutputStream os = urlConnection.getOutputStream();
        os.write(datas);
        os.flush();
        os.close();
        if (urlConnection.getResponseCode() == 200) {
            count++;
            LogUtil.d("第" + count + "个登录成功：" + info.getUserNum());
        } else {
            LogUtil.d("失败：");
        }
    }


    public void testStudentSign() throws Exception {
        for (int i=545;i<800;i++){
            final UserInfo user = new UserInfo();
            user.setUserNum("0000000"+i);
            user.setPassword("qqqqqq");
            user.setImei(CommonUtils.getIMEI());
            con(user);
          /*  userService = RetrofitUtil.create(UserService.class);
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
                        return;
                    }
                    if (userInfo!=null) {
//                        testGetMyCoures(userInfo.getTokenId(), userInfo.getUserNum());
                    } else {
                        LogUtil.d(rb.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LogUtil.d(t.toString());
                }
            });*/
        }
    }

    private void login(String name, String pwd, final boolean isTeacher) {
        final UserInfo user = new UserInfo();
        user.setUserNum(name);
        user.setPassword(pwd);
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
                if (userInfo!=null) {
                    if (isTeacher){
                        return;
                    }
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

    private void startRollCall(CourseInfo info){
        Map<String, Object> params = new HashMap<>();

        params.put("tokenId", App.userInfo.getTokenId());
        params.put("courseNo", info.getCourseNo());
        params.put("courseId", info.getId());
        params.put("courseName", info.getCourseName());
        params.put("userNum", App.userInfo.getUserNum());
        params.put("realName", App.userInfo.getName());
//        params.put("xPosition",latitude);
//        params.put("yPosition",longitude);
        params.put("xPosition",113.02954);
        params.put("yPosition",22.622995);
        RollCallService callService = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = callService.startCallName(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    LogUtil.d("已开始点名");
                } else {
                    LogUtil.d(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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