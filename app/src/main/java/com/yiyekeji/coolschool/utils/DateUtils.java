package com.yiyekeji.coolschool.utils;

import android.support.annotation.NonNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
    private long lNow = System.currentTimeMillis();
    private Calendar cNow = Calendar.getInstance();
    private Date dNow = new Date(lNow);
    private Timestamp tNow = new Timestamp(lNow);
    private java.sql.Date today = new java.sql.Date(lNow);
    private java.sql.Time now = new java.sql.Time(lNow);

    public static Date dateStringToDate(@NonNull String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 一天的MilliSecond数
     */
    private static final long DAY_MILLI = 24 * 60 * 60 * 1000;

    /**
     * 日期格式常量 Exp:1970-01-01
     */
    public static final String DATE_FORMAT_LINE = "yyyy-MM-dd";

    /**
     * 默认构造方法
     */
    public DateUtils() {

    }

    /**
     * 得到给定日期N天(前)后的日期
     *
     * num为正时表示N天后 反之前
     * @param num
     * @return
     */
    public static String beforeOrAfterDays(String datestr, int num) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date1=null;
        try {
            date1 = format.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_YEAR, num);
        return format.format(c.getTime());
    }

    /**
     * 得到给定日期N天(前)后的日期
     *
     * num为正时表示N天后 反之前 精确到秒
     * @param num
     * @return
     */
    public static String beforeOrAfterDaysSS(String datestr, int num) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date1=null;
        try {
            date1 = format.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_YEAR, num);
        return format.format(c.getTime());
    }



    /**
     * 得到星期
     * 如周三，周二
     * @param c
     * @return
     */
    public static String getDay2(Calendar c) {
        if (c != null) {
            int i = c.get(Calendar.DAY_OF_WEEK);
            i = i-1;
            if(i==1){
                return "周一";
            }else if(i==2){
                return "周二";
            }else if(i==3){
                return "周三";
            }else if(i==4){
                return "周四";
            }else if(i==5){
                return "周五";
            }else if(i==6){
                return "周六";
            }else if(i==0){
                return "周日";
            }else if(i==7){
                return "周日";
            }
        }
        return "";
    }

    /**
     *
     * 获取某个月的最后一天 格式2015-12-30
     * @param date  格式如“2015-12-12”
     * @return
     */
    public static String getLastDayOfMonth(String date)
    {
        String[] dates=date.split("-");
        Calendar cal = Calendar.getInstance();
        int year =Integer.valueOf(dates[0]);
        int month =Integer.valueOf(dates[1]);
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 获得前count个月份 第一天
     * @param date 格式2015-12-30
     * @param count
     * @return
     */
    public static String getSeveralMonth(String date,int count){
        //定义日期格式
        String[] dates=date.split("-");
        Calendar cal = Calendar.getInstance();
        int year =Integer.valueOf(dates[0]);
        int month =Integer.valueOf(dates[1]);
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取当前时间的前几个月
        cal.add(Calendar.MONTH,-count);
        cal.set(Calendar.DAY_OF_MONTH,1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String severalMonth = sdf.format(cal.getTime());

        return severalMonth;
    }

    /**
     * 日期格式化
     *
     * @param c
     * @param pattern
     * @return
     */
    public static String format(Calendar c, String pattern) {
        Calendar calendar = null;
        if (c != null) {
            calendar = c;
        } else {
            calendar = Calendar.getInstance();
        }
        if (pattern == null || pattern.equals("")) {
            pattern = "yyyy年MM月dd日 HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(calendar.getTime());
    }


    /**
     * 得到当前时间的字符串表示 格式2010-02-02 12:12:12
     *\
     * 注意HH大写 否则返回的是12小时制
     * @return
     */
    public static String getTimeString() {
        return format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 比较两个日期的大小
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//日期格式控制
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    public static Date getDate(String DATE1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//日期格式控制
        try {
            Date dt1 = df.parse(DATE1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isBeforeToday(String DATE1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//日期格式控制
        try {
            Date dt1 = df.parse(DATE1);
            Date date=Calendar.getInstance().getTime();
            if (dt1.before(date)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 比较两个日期的大小,返回较大的
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static String compare_date2(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//日期格式控制
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return DATE1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return DATE2;
            } else {
                return DATE1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return DATE1;
    }

    /**
     *	将字符串转换成日期类型
     * @param dateStr	日期字符串
     * @param styleStr	样式字符串,如:yyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date toDate(String dateStr, String styleStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(styleStr);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException pe) {
            pe.printStackTrace();
            date = null;
        }
        return date;
    }

    public static Date toDatess(String dateStr) {
        return toDate(dateStr,"yyyy-MM-dd HH:mm:ss");
    }

    public static Date toDate(String dateStr) {
       return toDate(dateStr,"yyyy-MM-dd");
    }
    /**
     *	求两个日期间隔的天数
     * @param startDate
     * @param endDate
     * @return
     * 求两个日期间隔的天数
     */
    public static int getBetweenDayNumber(Date startDate, Date endDate) {
        long dayNumber = 0;
        dayNumber = (endDate.getTime() - startDate.getTime()) / DateUtils.DAY_MILLI;

        return (int)dayNumber;
    }

    /**
     * 判断时间是否在时间段内
     * @return
     */
    public static boolean isInDate(String strDateBegin, String strDateEnd) {
        String strDate = getTimeString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式控制
        try {
            Date date1 = df.parse(strDate);
            Date date2 = df.parse(strDateBegin);
            Date date3 = df.parse(strDateEnd);

            if (date1.after(date2)&&date1.before(date3)){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     * 求两个日期间隔的天数
     * @param startDateStr
     * @param endDateStr 2014-03-14
     * @return
     */
    public static int cal_daysBetween(String startDateStr, String endDateStr) {
        long dayNumber = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_LINE);
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            dayNumber = (endDate.getTime() - startDate.getTime()) / DateUtils.DAY_MILLI;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int)dayNumber;
    }

    public static String subTimeFromMin(String dateSting){
        if (dateSting.length()<5){
            return "";
        }
        return dateSting.substring(5, dateSting.length());
    }


    /**
     * 得到两个日期间的秒数
     * @param startDate
     * @param endDate
     * @return
     */
    public static int cal_secBetween(Date startDate, Date endDate) {
        long dayNumber = 0;
        try {
            dayNumber = (endDate.getTime() - startDate.getTime()) / (1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int)dayNumber;
    }


    public static String getTimeStringNonM() {
        return format(Calendar.getInstance(), "yyyy-MM-dd");
    }
}

