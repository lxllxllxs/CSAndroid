package com.yiyekeji.coolschool.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Administrator on 2017/4/7/007.
 */
public class FileUtils {
    /**
     * 获取指定文件大小 以kb为单位
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size / 1024/1024;
    }
}
