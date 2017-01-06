package com.yiyekeji.coolschool.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 选择省份城市
 * Created by lxl on 2016/8/20.
 */

public class SPUtils
{  
    /** 
     * 保存在手机里面的文件名 
     */  
    public static final String FILE_NAME = "share_data";
    private static String serStr;
  
    /** 
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 
     *  
     * @param context 
     * @param key 
     * @param object 
     */  
    public static void put(Context context, String key, Object object)
    {  
  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
  
        if (object instanceof String)  
        {  
            editor.putString(key, (String) object);  
        } else if (object instanceof Integer)  
        {  
            editor.putInt(key, (Integer) object);  
        } else if (object instanceof Boolean)  
        {  
            editor.putBoolean(key, (Boolean) object);  
        } else if (object instanceof Float)  
        {  
            editor.putFloat(key, (Float) object);  
        } else if (object instanceof Long)  
        {  
            editor.putLong(key, (Long) object);  
        } else  
        {
//            editor.putString(key, object.toString());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                serStr= byteArrayOutputStream.toString("ISO-8859-1");
                serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
                objectOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.putString(key,serStr);

        }  
  
        SharedPreferencesCompat.apply(editor);  
    }  
  
    /** 
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值 
     *  
     * @param context 
     * @param key 
     * @param defaultObject 
     * @return 
     */  
    public static Object get(Context context, String key, Object defaultObject)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
  
        if (defaultObject instanceof String)  
        {  
            return sp.getString(key, (String) defaultObject);  
        } else if (defaultObject instanceof Integer)  
        {  
            return sp.getInt(key, (Integer) defaultObject);  
        } else if (defaultObject instanceof Boolean)  
        {  
            return sp.getBoolean(key, (Boolean) defaultObject);  
        } else if (defaultObject instanceof Float)  
        {  
            return sp.getFloat(key, (Float) defaultObject);  
        } else if (defaultObject instanceof Long)  
        {  
            return sp.getLong(key, (Long) defaultObject);  
        }else {
            //实现了Serializable接口才可用 parcelable不行
            String str=sp.getString(key, (String) defaultObject);
            String redStr = null;
            try {
                redStr = java.net.URLDecoder.decode(str, "UTF-8");
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object =  objectInputStream.readObject();
                objectInputStream.close();
                byteArrayInputStream.close();
                return object;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static String getString(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static String getString(Context context, String key,String defaultValue)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }
    /** 
     * 移除某个key值已经对应的值 
     * @param context 
     * @param key 
     */  
    public static void remove(Context context, String key)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
        editor.remove(key);  
        SharedPreferencesCompat.apply(editor);  
    }  
  
    /** 
     * 清除所有数据 
     * @param context 
     */  
    public static void clear(Context context)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
        editor.clear();  
        SharedPreferencesCompat.apply(editor);  
    }  
  
    /** 
     * 查询某个key是否已经存在 
     * @param context 
     * @param key 
     * @return 
     */  
    public static boolean contains(Context context, String key)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        return sp.contains(key);  
    }  
  
    /** 
     * 返回所有的键值对 
     *  
     * @param context 
     * @return 
     */  
    public static Map<String, ?> getAll(Context context)
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        return sp.getAll();  
    }  
  
    /** 
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类 
     *  
     * @author zhy 
     *  
     */  
    private static class SharedPreferencesCompat  
    {  
        private static final Method sApplyMethod = findApplyMethod();
  
        /** 
         * 反射查找apply的方法 
         *  
         * @return 
         */  
        @SuppressWarnings({ "unchecked", "rawtypes" })  
        private static Method findApplyMethod()  
        {  
            try  
            {  
                Class clz = SharedPreferences.Editor.class;  
                return clz.getMethod("apply");  
            } catch (NoSuchMethodException e)  
            {  
            }  
  
            return null;  
        }  
  
        /** 
         * 如果找到则使用apply执行，否则使用commit 
         *  
         * @param editor 
         */  
        public static void apply(SharedPreferences.Editor editor)  
        {  
            try  
            {  
                if (sApplyMethod != null)  
                {  
                    sApplyMethod.invoke(editor);  
                    return;  
                }  
            } catch (IllegalArgumentException e)  
            {  
            } catch (IllegalAccessException e)  
            {  
            } catch (InvocationTargetException e)
            {  
            }  
            editor.commit();  
        }  
    }  
  
}  