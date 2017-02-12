package com.example.stu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;


public class SoapUtils {

	public static void call(final Map<String,String> params,
							final Map<String,String> headers,
							final String urlString,
							final CallBack callBack) {
		Runnable requestRunnable = new Runnable() {
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setReadTimeout(5000);
					urlConnection.setConnectTimeout(5000);
					if (headers!=null&&headers.size()>0){
						Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry map=it.next();
							urlConnection.addRequestProperty(map.getKey().toString(),map.getValue().toString());
						}
					}
					urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
					urlConnection.setDoOutput(true);
		            urlConnection.setDoInput(true);
					// 构建请求参数
					StringBuffer sb = new StringBuffer();
					if (!params.isEmpty()) {
						for (Map.Entry<String, String> e : params.entrySet()) {
							sb.append(e.getKey());
							sb.append("=");
							sb.append(e.getValue());
							sb.append("&");
						}
						sb.substring(0, sb.length() - 1);
					}
					urlConnection.getOutputStream().write(sb.toString().getBytes());
		            if (urlConnection.getResponseCode() == 200) {
						callBack.onSuccess(urlConnection);
		            }else {
						callBack.onFailure(urlConnection.getResponseCode());
		            }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPools.getInstance().addRunnable(requestRunnable);
	}


	public interface CallBack {
		public void onSuccess(HttpURLConnection connection) throws IOException;
		public void onFailure(int errorCode);
	}
	
}
