package com.yiyekeji.coolschool.test;

import com.yiyekeji.coolschool.utils.ThreadPools;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lxl on 2017/1/16.
 */
public class TestHashTable {

    private Hashtable<Integer, String> hashtable = new Hashtable<>();
    public static void main(String[] args){
        TestHashTable testHashTable=new TestHashTable();
        testHashTable.addToHashTable();

    }

    int i=0;
    private void addToHashTable(){
        for (;i<500;i++) {
            ThreadPools.getInstance().addRunnable(new Runnable() {
                @Override
                public void run() {
                    hashtable.put(i,i+"");
                }
            });
        }
        timer.schedule(timerTask,10*1000);
    }


    private Timer timer=new Timer();
    private TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            Iterator iterator = hashtable.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                int key= (int) entry.getKey();
                String value = (String) entry.getValue();
                System.out.println(key + "==" + value);
            }
        }
    };

}
