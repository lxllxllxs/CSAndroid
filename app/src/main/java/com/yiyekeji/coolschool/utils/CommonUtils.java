package com.yiyekeji.coolschool.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.yiyekeji.coolschool.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lxl on 2017/1/11.
 */
public class CommonUtils {
    private int cellId = 0;
    private int lac = 0;

    public static String getIMEI(){
        TelephonyManager TelephonyMgr = (TelephonyManager) App.getContext().getSystemService(App.getContext().TELEPHONY_SERVICE);
                return TelephonyMgr.getDeviceId();
            }


    /** 获取单个App版本号 **/
    public static int getAppVersion()  {
        int versionCode=0;
        PackageManager packageManager = App.getContext().getPackageManager();
        try {
            versionCode= packageManager
                    .getPackageInfo(App.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode ;
    }


    /**
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context){
        String result="";
        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine="";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result=sb.toString().toLowerCase();
        } catch(IOException ex){
        }
        return (!result.contains("arm")) || (result.contains("intel")) || (result.contains("amd"));
    }
}
