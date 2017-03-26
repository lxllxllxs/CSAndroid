package com.yiyekeji.coolschool.api;

import com.yiyekeji.coolschool.utils.ConstantUtils;
import com.yiyekeji.coolschool.utils.SoapUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by lxl on 2017/2/13.
 */
public class WyuStuSystemApi {

	public static void main(String[] a) throws IOException {
		WyuStuSystemApi api=new WyuStuSystemApi();
		String b=api.login("3114004199","941119");
		System.out.println("这里成功就返回lastCookie"+b);
		try {
			api.getCourseHtmlString(b);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	private  String getFirstCookie() throws IOException {
		final String main="http://jwc.wyu.cn/student/";
		Map<String,String> headers= new HashMap<>();
		Map<String, String> params = new HashMap<>();
		HttpURLConnection connection= SoapUtils. call(params, headers,main);
		if (connection!=null&&connection.getResponseCode()==200){
			String  firstCookie = "Set-Cookie"+"="+connection.getHeaderField("Set-Cookie");
			System.out.println("获取firstCookie成功！"+firstCookie);
			return firstCookie;
		}
		return "error";
	}

	private  Map<String,String> getVail(final String login,final String pwd,final String firstCookie) throws IOException {
		Map<String,String> headerMap= new HashMap<>();
		Map<String, String> params = new HashMap<>();
		headerMap.put("Cookie",firstCookie);
		headerMap.put("Referer","http://jwc.wyu.cn/student/body.htm");
		HttpURLConnection connection=SoapUtils.call(params, headerMap, "http://jwc.wyu.cn/student/rndnum.asp");
		if (connection!=null&&connection.getResponseCode()==200){
			String headers=connection.getHeaderField("Set-Cookie");
			if (headers!=null&&!headers.equals("")){
				String validate=headers.substring(headers.indexOf("=")+1,headers.indexOf(";"));
				String Cookie =firstCookie+ "; NGID=6a158449-ea8d-7afa-73f3-663f48f846b1; 6a158449-ea8d-7afa-73f3-663f48f846b1=http%3A//jwc.wyu.cn/student/body.htm;LogonNumber=" + validate;
				//设置lastCookie
				String lastCookie = Cookie.substring(0, Cookie.length()-validate.length());
				params.clear();

				System.out.println("Cookie"+lastCookie);
				System.out.println("UserCode"+login);
				System.out.println("UserPwd"+pwd);
				System.out.println("Validate"+validate);

				String Submit ="%CC%E1+%BD%BB";
				params.put("Cookie",Cookie);
				params.put("UserCode", login);
				params.put("UserPwd", pwd);
				params.put("Validate", validate);
				params.put("Submit", Submit);
				return params;
			}
		}
		return null;
	}
	public  String login(final String login, final String pwd) throws IOException {
		Map<String, String> headers = new HashMap<>();
		String firstCookie=getFirstCookie();
		System.out.println(firstCookie);
		if ("error".equals(firstCookie)) {
			return ConstantUtils.LOGIN_SYS_ERROR;
		}
		Map<String, String> params = getVail(login, pwd, firstCookie);
		if (params!=null&&params.get("Cookie")!=null){
			headers.put("Cookie", params.get("Cookie"));
		}
		headers.put("Referer", "http://jwc.wyu.cn/student/body.htm");

		HttpURLConnection connection = SoapUtils.call(params,headers,"http://jwc.wyu.cn/student/logon.asp");
		if (connection != null && connection.getResponseCode() == 200) {
			System.out.println("校验：" + connection.getContentLength());
			System.out.println("校验：" + connection.getHeaderField("Content-Length"));
			if (129 >= Integer.valueOf(connection.getHeaderField("Content-Length"))) {
				System.out.println("error");
				return ConstantUtils.LOGIN_PSW_ERROR;
			}
			if (params==null){
				return ConstantUtils.LOGIN_SYS_ERROR;
			}
			return params.get("Cookie");
		}
		return ConstantUtils.LOGIN_CONTACT_ERROR;
	}

	/**
	 * 获取包含学生的课程信息的htmlString
	 * @param lastCookie
	 * @throws TimeoutException
	 * @throws IOException
     */
	public  void getCourseHtmlString(String lastCookie) throws TimeoutException,IOException {
		Map<String,String> headers= new HashMap<>();
		headers.put("Cookie",lastCookie);
		headers.put("Referer", "http://jwc.wyu.cn/student/menu.asp");
		HttpURLConnection connection = SoapUtils.call(new HashMap<String, String>(), headers, "http://jwc.wyu.cn/student/f3.asp");

		if (connection != null && connection.getResponseCode() == 200) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte buffer[] = new byte[1024];
			while ((len = connection.getInputStream().read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			String result = new String(baos.toByteArray(),"gbk");
			System.out.println(result);
		} else {
			System.out.println("获取失败！");
		}
	}

	public Map<Integer, Map<Integer, List<Lesson>>> parseHtmlForLesson(String xml){
		return	HtmlParser.parseHtmlForLesson(xml);
	}

}
