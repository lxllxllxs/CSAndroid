package com.yiyekeji.coolschool.utils;

import java.io.IOException;
import java.io.InputStream;

public class SoapUtils {

/*	public static void call(final String method,final AbstractMessage msg,final CallBack callBack) {
		Runnable requestRunnable = new Runnable() {
			public void run() {
				try {
					URL url = new URL(Config.BASE_URL+method);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setReadTimeout(5000);
					urlConnection.setConnectTimeout(5000);
					urlConnection.setRequestProperty("Content-Type", "application/octet-stream;charset=iso8859-1");
					urlConnection.setDoOutput(true);
		            urlConnection.setDoInput(true);
					msg.writeTo(urlConnection.getOutputStream());
		            if (urlConnection.getResponseCode() == 200) {
						callBack.onSuccess(urlConnection.getInputStream());
						LogUtil.d("SoapUtil:call","successful");
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
	*/


	public interface CallBack {
		public void onSuccess(InputStream inputStream) throws IOException;
		public void onFailure(int errorCode);
	}
	
}
