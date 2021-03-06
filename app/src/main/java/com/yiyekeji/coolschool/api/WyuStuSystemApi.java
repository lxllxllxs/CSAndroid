package com.yiyekeji.coolschool.api;

import com.yiyekeji.coolschool.bean.StudentInfo;
import com.yiyekeji.coolschool.utils.ConstantUtils;
import com.yiyekeji.coolschool.utils.SoapUtils;

import org.apache.http.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
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
			api.getCourse(b);
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


	/*
  * 获取学生信息
  */
	public StudentInfo getStudentInfo(String userID) throws IOException {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> params = new HashMap<>();
		headers.put("Referer","http://jwc.wyu.edu.cn/student/qinfo.htm");
		params.put("S", userID);
		params.put("button","%B2%E9%D1%AF");
		HttpURLConnection connection = SoapUtils.call(params,headers,"http://jwc.wyu.edu.cn/student/qinfo.asp");
		if (connection != null && connection.getResponseCode() == 200) {
			StringBuilder sb = new StringBuilder();
			int count=0;
			byte[] bytes=new byte[1024];
			InputStream is = connection.getInputStream();
			while((count=is.read(bytes))!=-1){
				sb.append(new String(bytes, 0, count,"gbk"));
			}
			String content = sb.toString();
			System.out.println("学生信息:"+content);
			return parseHtmlOfStudentInfos(content);
		}
		return null;
	}


	/*
  * 解析返回的学生信息的html 返回一个StudentInfo集合
  */
	public static StudentInfo parseHtmlOfStudentInfos(String html) {
		StudentInfo sInfo = null;
		Document doc = null;
		String[] stu = new String[8]; //暂时存放学生各种信息
		if (html != null && html != "") {
			doc = Jsoup.parse(html);
			int i = 0;//给stu加数据的循环标记
			Elements tds = doc.select("td");//获取所有标签为<td....的数据
			for (Element td : tds) {
				Elements bElements = td.select("b");
				if(!bElements.isEmpty()){ //若存在<b>标签的话，跳过
					continue;
				}else{
					stu[i++] = td.text().toString();
				}
			}
			sInfo = new StudentInfo(stu);
			return sInfo;
		}
		return null;
	}

	/*
         * 获取成绩
         * fixme 这里需要大大延长时间
         */
	public String getScoreList(String lastCookie) throws TimeoutException,ParseException, IOException{
		List<ScoreRecord> list = new ArrayList<ScoreRecord>();


		Map<String,String> headers= new HashMap<>();
		headers.put("Cookie",lastCookie);
		headers.put("Referer", "http://jwc.wyu.cn/student/menu.asp");
		HttpURLConnection connection = SoapUtils.call(new HashMap<String, String>(), headers, "http://jwc.wyu.edu.cn/student/f4_myscore.asp");

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
//			return parseHtmlForLesson(result);
			return result;
		} else {
			System.out.println("获取失败！");
			return null;
		}
	}
	/**
	 * 获取包含学生的课程信息的htmlString
	 * @param lastCookie
	 * @throws TimeoutException
	 * @throws IOException
     */
	public   String getCourse(String lastCookie) throws TimeoutException,IOException {
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
//			return parseHtmlForLesson(result);
			return result;
		} else {
			System.out.println("获取失败！");
			return null;
		}
	}

	private Map<Integer, Map<Integer, List<Lesson>>> parseHtmlForLesson(String xml){
		return	HtmlParser.parseHtmlForLesson(xml);
	}

}
