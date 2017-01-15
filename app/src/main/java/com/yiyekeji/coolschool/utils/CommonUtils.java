package com.yiyekeji.coolschool.utils;

import android.telephony.TelephonyManager;

import com.yiyekeji.coolschool.App;

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


}
