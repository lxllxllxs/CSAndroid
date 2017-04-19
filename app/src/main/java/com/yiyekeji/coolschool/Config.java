package com.yiyekeji.coolschool;

import android.os.Environment;

/**
 * Created by lxl on 2016/12/25.
 */
public class Config {
//    public static final String BASE_URL="http://59.110.143.46:8080/cs/";
    public static final String BASE_URL="http://192.168.1.100:9090/cs/";
//    public static final String BASE_URL="http://123.207.13.169:8080/cs/";//正式环境
    public static final boolean DEBUG=true;
    public static final String TEMP_FILE="/coolschool/";
    public static final String SD_PATH= Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String IMG_TEMP_PATH=SD_PATH+"/tempImage/";

}
