package com.yiyekeji.coolschool.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class SoapUtils {
	/**
	 * 专为WyuAPi使用
	 * @param params
	 * @param headers
	 * @param urlString
     * @return
     */
	public static HttpURLConnection call(final Map<String,String> params,
										 final Map<String,String> headers,
										 final String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setReadTimeout(30000);
			urlConnection.setConnectTimeout(30000);
			if (headers!=null&&headers.size()>0){
				Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry map=it.next();
					urlConnection.addRequestProperty((String) map.getKey(),(String) map.getValue());
				}
			}
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			// 构建请求参数
			StringBuffer sb = new StringBuffer();
			// FIXME: 2017/5/9 这里存在空指针???
			if (params == null) {
				return null;
			}
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
			return urlConnection;
			/*if (urlConnection.getResponseCode() == 200) {
				return true;
			}else {
				return false;
			}*/
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public interface CallBack {
		public void onSuccess(InputStream inputStream) throws IOException;
		public void onFailure(int errorCode);
	}
	
}
