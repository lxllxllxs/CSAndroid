package com.yiyekeji.coolschool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lxl on 2017/1/5.
 */
public class App extends Application{
    public static  boolean isLogin;
    public static UserInfo userInfo = new UserInfo();
    public static List<Activity> activityList = new ArrayList<>();
    public static Context context;
    public static UserInfo otherSide;//可用来判断当前时与哪个用户聊天
    public static void clearAllCache(){
        userInfo = new UserInfo();//指向新对象
        isLogin=false;
    }

    public static boolean isExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

//    PushAgent mPushAgent;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        initPushAgent();
    }

    private void initPushAgent() {
       /* mPushAgent= PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        ThreadPools.getInstance().addRunnable(new Runnable() {
            @Override
            public void run() {
                mPushAgent.register(new IUmengRegisterCallback() {
                    @Override
                    public void onSuccess(String deviceToken) {
                        //注册成功会返回device token
                        LogUtil.d("App:onCreate:设备注册成功！", deviceToken);
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        LogUtil.d("App:onCreate:设备注册失败！", s + s1);
                    }
                });
            }
        });*/
    }

    public static Context getContext(){
        return context;
    }

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void removeAllActivity(){
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public static String geTokenId() {
        if (tokenOutOfDate()){

        }

        return userInfo.getTokenId();
    }


/*

    private void login(){
        final UserInfo user = new UserInfo();
        if (SPUtils.contains(this,LOGIN_NAME)){
            user.setUserNum(SPUtils.getString(this,LOGIN_NAME));
        }
        if (SPUtils.contains(this,PWD)){
            user.setPassword(SPUtils.getString(this,PWD);
        }
        user.setImei(CommonUtils.getIMEI());
        UserService userService = RetrofitUtil.create(UserService.class);
        Call<ResponseBody> call= userService.login(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                UserInfo  userInfo= GsonUtil.fromJSon(jsonString,UserInfo.class,"userInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (userInfo!=null) {
                    showShortToast("成功登录！");
                    userInfo.setPassword("");//清除密码
                    App.userInfo=userInfo;
                    Intent intent = new Intent(LoginActivity.this, MainViewpagerActivity.class);
                    startActivity(intent);

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
*/

    /**
     * 判断token是否过期
     * @return
     */
    private static boolean tokenOutOfDate() {
        String oldTimeString = userInfo.getLastTime();
        Date oldDate = DateUtils.toDate(oldTimeString,
                "yyyy-MM-dd HH:mm:ss");
        Date nowDate = DateUtils.toDate(DateUtils.getTimeString(),
                "yyyy-MM-dd HH:mm:ss");
        int between = DateUtils.cal_secBetween(oldDate, nowDate);//罪魁祸首 前后都要用24小时制
        if (between > 30 * 60) {//先定30分钟
            return true;
        }
        return false;
    }

}
