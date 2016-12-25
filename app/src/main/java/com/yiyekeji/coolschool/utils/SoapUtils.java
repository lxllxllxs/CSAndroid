package com.yiyekeji.coolschool.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.protobuf.AbstractMessage;
import com.yiyekeji.coolschool.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapUtils {
	private static Handler mainHandler;

	public static void call(final AbstractMessage msg,final CallBack callBack) {
		Runnable requestRunnable = new Runnable() {
			public void run() {
				try {
					URL url = new URL(Config.BASE_URL+"login");
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
	
	
	private static void initHandler() {
		if(mainHandler == null) {
			mainHandler = new Handler(Looper.getMainLooper());
		}
	}
	

	public interface CallBack {
		public void onSuccess(InputStream inputStream) throws IOException;
		public void onFailure(int errorCode);
	}
	
}
