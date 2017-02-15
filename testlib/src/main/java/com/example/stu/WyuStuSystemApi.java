package com.example.stu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
/**
 * Created by lxl on 2017/2/13.
 */
public class WyuStuSystemApi {

    public  void getFirstCookie(final String login,final String pwd){
        final String main="http://jwc.wyu.cn/student/";
        Map<String,String> headers= new HashMap<>();
        Map<String, String> params = new HashMap<>();
        SoapUtils. call(params,  headers,main, new SoapUtils.CallBack() {
            @Override
            public void onSuccess(HttpURLConnection connection) throws IOException {
//                String firstCookie=headers.substring(0,headers.indexOf(";"));
                String  firstCookie = "Set-Cookie"+"="+connection.getHeaderField("Set-Cookie");
                System.out.println("firstCookie:"+firstCookie);
                getVail(login,pwd,firstCookie);
            }
            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    private  void getVail(final String login,final String pwd,final String firstCookie){
        Map<String,String> headers= new HashMap<>();
        Map<String, String> params = new HashMap<>();
        headers.put("Cookie",firstCookie);
        headers.put("Referer","http://jwc.wyu.cn/student/body.htm");
        SoapUtils.call(params, headers, "http://jwc.wyu.cn/student/rndnum.asp", new SoapUtils.CallBack() {
            @Override
            public void onSuccess(HttpURLConnection connection) throws IOException {
                String headers=connection.getHeaderField("Set-Cookie");
                if (headers!=null&&!headers.equals("")){
                    String validate=headers.substring(headers.indexOf("=")+1,headers.indexOf(";"));
                    String Cookie =firstCookie+ "; NGID=6a158449-ea8d-7afa-73f3-663f48f846b1; 6a158449-ea8d-7afa-73f3-663f48f846b1=http%3A//jwc.wyu.cn/student/body.htm;LogonNumber=" + validate;
                    //设置lastCookie
                    String	lastCookie = Cookie.substring(0, Cookie.length()-validate.length());
                    login(Cookie,lastCookie,validate,login,pwd);
                }
            }
            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    private  void login(String cookie, String lastCookie, final String validate, final String login, final String pwd){
        Map<String,String> headers= new HashMap<>();
        Map<String, String> params = new HashMap<>();
        System.out.println("lastCookie:"+cookie);
        System.out.println("lastCookie:"+lastCookie);
        System.out.println("validate:"+validate);
        System.out.println("login:"+login);
        System.out.println("pwd:"+pwd);

        headers.put("Cookie",cookie);
        headers.put("Referer","http://jwc.wyu.cn/student/body.htm");

        String Submit ="%CC%E1+%BD%BB";
        params.put("UserCode", login);
        params.put("UserPwd", pwd);
        params.put("Validate", validate);
        params.put("Submit", Submit);

        SoapUtils.call(params, headers, "http://jwc.wyu.cn/student/logon.asp", new SoapUtils.CallBack() {
            @Override
            public void onSuccess(HttpURLConnection connection) throws IOException {
                if (connection.getResponseCode()==200){
                    System.out.println("校验："+connection.getContentLength());
                    System.out.println("校验："+connection.getHeaderField("Content-Length"));
                    if(129 >= Integer.valueOf(connection.getHeaderField("Content-Length"))){
                        System.out.print("error");
                        return;
                    }
                    try {
                        getCourseMap(lastCookie);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    System.out.print("succesas");
                }
            }
            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    /*
 * 获取课表
 */
    public  void getCourseMap(String lastCookie) throws TimeoutException,IOException {
        Map<String,String> headers= new HashMap<>();
        headers.put("Cookie",lastCookie);
        headers.put("Referer", "http://jwc.wyu.cn/student/menu.asp");
        SoapUtils.call(new HashMap<>(), headers, "http://jwc.wyu.cn/student/f3.asp", new SoapUtils.CallBack() {
            @Override
            public void onSuccess(HttpURLConnection connection) throws IOException {
                if (connection.getResponseCode()==200){
                    StringBuilder sb = new StringBuilder();
                    int count=0;
                    byte[] bytes=new byte[1024];
                    InputStream is = connection.getInputStream();
                    while((count=is.read(bytes))!=-1){
                        sb.append(new String(bytes, 0, count,"gbk"));
                    }
                    String content = sb.toString();
                    System.out.println("课程表信息:"+content);
                }
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }
}
