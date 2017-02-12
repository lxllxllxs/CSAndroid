package com.yiyekeji.coolschool.api;

import com.yiyekeji.coolschool.WyuStuSysConfig;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 学生子服务系统的相关操作
 * @author 世财
 *
 */
public class SubSystemApi {
	private String userID;
	private String userPSW;
	private String validate; //验证码
	private String firstCookie;  //登录用
	private String lastCookie;	  //保持登录状态

	public SubSystemApi(){

	}

	/*
         * 获取验证码
         */
	private void getValidate() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://jwc.wyu.cn/student/rndnum.asp");
		get.setHeader("Referer","http://jwc.wyu.cn/student/body.htm");
		get.setHeader("Cookie",firstCookie);
		HttpResponse response;
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);//连接时间
		HttpConnectionParams.setSoTimeout(client.getParams(), 10000);//请求时间
		response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HeaderElement[] he = response.getFirstHeader("Set-Cookie")
					.getElements();
			validate = he[0].getValue();
		}
		client.getConnectionManager().shutdown();
		if(validate == null){
			throw new Exception();
		}
	}

	/*
	 * 获取要发送的Cookie
	 */
	private void getFirstCookie() throws Exception {
		OkHttpClient clien = new OkHttpClient.Builder()
				.connectTimeout(15000, TimeUnit.SECONDS)
				.readTimeout(15000, TimeUnit.SECONDS)
				.writeTimeout(15000, TimeUnit.SECONDS)
				.build();
		Request request =new Request.Builder()
				.url(WyuStuSysConfig.STUDENT_SYSTEM_URL)
				.build();
		Response response=clien.newCall(request).execute();

		if (response.code() == 200) {
			String value= response.header("Set-Cookie");
			firstCookie = "Set-Cookie"+"="+value;
		}
		if(firstCookie == null){
			throw new Exception();
		}
		getValidate();
		firstCookie += "; NGID=6a158449-ea8d-7afa-73f3-663f48f846b1; 6a158449-ea8d-7afa-73f3-663f48f846b1=http%3A//jwc.wyu.cn/student/body.htm;LogonNumber=" + validate;
		lastCookie = firstCookie.substring(0, firstCookie.length() - validate.length());
	}

	/*
	 * 登录
	 */
	public int login(String userID, String userPSW) throws Exception {
		getFirstCookie();
		this.userID = userID;
		this.userPSW = userPSW;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(WyuStuSysConfig.STU_SYS_LOGIN_URL);
		HttpResponse response;
		// login
		String UserCode = userID;
		String UserPwd = userPSW;
		String Validate = validate;
		String Submit = "%CC%E1+%BD%BB";
		// 构建表头
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("UserCode", UserCode));
		parameters.add(new BasicNameValuePair("UserPwd", UserPwd));
		parameters.add(new BasicNameValuePair("Validate", Validate));
		parameters.add(new BasicNameValuePair("Submit", Submit));
		HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
		post.setEntity(entity);
		post.setHeader("Referer", "http://jwc.wyu.cn/student/body.htm");
		post.setHeader("Cookie", firstCookie);
		System.out.println("firstCookie:"+firstCookie);
		System.out.println("lastCookie:"+lastCookie);
		System.out.println("validate:"+validate);
		System.out.println("login:"+userID);
		System.out.println("pwd:"+userPSW);
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);//连接时间
		HttpConnectionParams.setSoTimeout(client.getParams(), 10000);//请求时间
		response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			System.out.println("校验："+response.getFirstHeader("Content-Length"));
			System.out.println("校验："+response.getFirstHeader("Content-Length").getValue());
			if(129 == Integer.valueOf(response.getFirstHeader("Content-Length").getValue())){
				System.out.println("error");
				client.getConnectionManager().shutdown();
				return 0;
			}
			System.out.println("success");
			client.getConnectionManager().shutdown();
			return 1;
		} else {
			client.getConnectionManager().shutdown();
			return 3;
		}
	}
}
