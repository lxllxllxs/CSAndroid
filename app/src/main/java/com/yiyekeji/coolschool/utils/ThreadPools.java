package com.yiyekeji.coolschool.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPools {
	private static ThreadPools instance;
	private static ExecutorService threadPool;
	private static final int COUNT = Runtime.getRuntime().availableProcessors() * 2;
	
	private ThreadPools() {
		
	}
	
	public static ThreadPools getInstance() {
		if(instance == null) {
			instance = new ThreadPools();
		}
		return instance;
	}
	
	private static void init() {
		if(threadPool == null) {
			threadPool = Executors.newFixedThreadPool(COUNT);
		}
	}
	
	public void addRunnable(Runnable runnable) {
		init();
		threadPool.execute(runnable);
	}
	
	public void shutDown() {
		if(threadPool!=null && !threadPool.isShutdown()) {
			threadPool.shutdown();
			threadPool = null;
		}
	}
	
}
